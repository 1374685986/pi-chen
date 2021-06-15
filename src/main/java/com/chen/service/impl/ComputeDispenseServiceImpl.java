package com.chen.service.impl;

import com.chen.dao.ComputeInfo;
import com.chen.dao.ComputeResultBitMapper;
import com.chen.dao.ComputeResultMapper;
import com.chen.entity.ComputeJob;
import com.chen.entity.ComputePool;
import com.chen.entity.ComputeResult;
import com.chen.entity.ComputeResultBit;
import com.chen.entity.vo.ComputeJobDto;
import com.chen.entity.vo.ComputeJobResult;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chen.service.ComputeDispenseService;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Service("computeDispenseServiceImpl")
public class ComputeDispenseServiceImpl implements ComputeDispenseService {

    public static int JOB_SIZE = 15; // 可输入

    // liu zhe ji suan 10 jin zhi
    private static final Character[] HEX_NUM = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

    // 位数
    private ConcurrentLinkedDeque<Long> jobs = new ConcurrentLinkedDeque<>();

    // 任务队列
    private ConcurrentLinkedDeque<ComputeJob> jobss = new ConcurrentLinkedDeque<>();

    // 计算结果
    private Set<ComputeJobResult> results = new HashSet<>();

    // 计算
    private ComputeResultHandler computeResultHandler;

    @Autowired
    private ComputeInfo computeInfo;


    private ComputeResultMapper computeResultMapper;

    @Autowired
    private ComputeResultBitMapper computeResultBitMapper;


    private HashMap<String, ComputePool> computePool = new HashMap<>();

    public ComputeDispenseServiceImpl(ComputeResultHandler computeResultHandler,ComputeResultMapper computeResultMapper){
        this.computeResultMapper=computeResultMapper;
        this.computeResultHandler=computeResultHandler;
        fillJob();
    }


    // 获取任务，synchronized 同步获得
    @Override
    public synchronized ComputeJobDto dispense() {
        if (jobs.isEmpty()) fillJob();
        ComputeJobDto job = new ComputeJobDto();
        job.setBit(getBit());
        return job;
    }

    @Override
    public synchronized void init(String token) {
        List<ComputeResultBit> resultBits = computeResultBitMapper.findNeedChecked(token);
        if (resultBits != null && resultBits.size() > 0){

            ComputePool pool = new ComputePool();
            pool.setToken(token);
            var bits = new ArrayDeque<Long>();

            for(var i:resultBits){
                bits.add(i.getDigit());
            }
            pool.setBitPool(bits);
            computePool.put(token,pool);
        }
    }

    @Override
    public synchronized ComputeJobDto dispense(String tokenId) {
        if (jobs.isEmpty()){
            fillJob();
        }
        ComputeJobDto job = new ComputeJobDto();
        if (computePool.containsKey(tokenId)){
            var result  = computePool.get(tokenId);
            Long bit = result.getBitPool().removeFirst();
            result.getBitPool().add(bit);
            job.setBit(bit);
            return job;
        }

        job.setBit(getBit());
        return job;
    }

    @Override
    public synchronized void reclaim(ComputeJobResult result) {
        if (computePool.containsKey(result.getProcessId())){
            try {
                computeResultHandler.put(result);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        //先检查,是否需要的计算结果
        long bits = result.getBit();
        if (!jobs.contains(bits)){
            return;
        }
        //加入结果集
        results.add(result);

        //保存到数据库
        try {
            computeResultHandler.put(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //从任务队列里移除
        jobs.remove(result.getBit());

        //这次的任务完成了
        if (jobs.size() == 0){
            // 添加新任务
            computeInfo.updateMaxBit(10L);
        }
    }

    @Override
    public synchronized Long getBit() {
        Long bit = jobs.pop();
        jobs.addLast(bit);
        return bit;
    }

    @Override
    public synchronized ComputeJobDto getJob(String token) {
        return null;
    }

    @Override
    public synchronized void fillJob() {
        ComputeResult result = computeResultMapper.findTopByOrderByEndIndexDesc();
        long startIndex  = 0L;
        if (result != null) {
            startIndex = result.getEndIndex(); // 开始执行的最后一位
        }
        for (int i = 0; i < ComputeDispenseServiceImpl.JOB_SIZE; i++) {
            jobs.add(startIndex + i);
        }
    }
}
