package com.chen.service.impl;

import com.chen.dao.ComputeResultBitMapper;
import com.chen.entity.ComputeRecord;
import com.chen.entity.ComputeResultBit;
import com.chen.entity.vo.ComputeJobResult;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;

@Service
public class ComputeResultHandler {
    private final LinkedBlockingDeque<ComputeJobResult> queue = new LinkedBlockingDeque<>();

    @Autowired
    private ComputeResultBitMapper computeResultBitMapper;

    public ComputeResultHandler(ComputeResultBitMapper computeResultBitMapper) {
        this.computeResultBitMapper = computeResultBitMapper;
        new Thread(()-> {
            try {
                exec();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /*执行*/
    private void exec() throws InterruptedException {
        while (true) {
            ComputeJobResult c = queue.take();
            handler(c);
        }
    }

    // 放入任务队列中
    public void put(ComputeJobResult c) throws InterruptedException {
        if (c == null){
            return;
        }
        queue.put(c);
    }

    // 处理
    private void handler(ComputeJobResult result) {
        try {
            var target = computeResultBitMapper.findTopByDigit(result.getBit());
            if (target == null) {
                //保存到数据库
                System.out.println("执行=====执行=====执行");
                ComputeResultBit bit = new ComputeResultBit();
                Date startTime = result.getStartTime();
                if (startTime==null) startTime = new Date();
//                bit.setComputeTime(result.getStartTime());
                bit.setComputeTime(startTime);
                bit.setChecked(true);
                bit.setDigit(result.getBit());
                bit.setResult(result.getResult());
                var record = new ComputeRecord();
                record.setToken(result.getProcessId());
                record.setComputeDate(new Date());
                record.setResult(result.getResult());
                System.out.println(bit);
                computeResultBitMapper.save(bit);
            }else {
                System.out.println("未执行====未执行=====未执行");
                var records =  target.getRecords();
                if (records == null){
                    records = new ArrayList<>();
                }
                var record = new ComputeRecord();
                record.setToken(result.getProcessId());
                record.setComputeDate(new Date());
                record.setResult(result.getResult());

                records.add(record);
                target.setRecords(records);
                computeResultBitMapper.save(target);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
