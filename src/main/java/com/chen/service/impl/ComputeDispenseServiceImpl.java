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

    public static int JOB_SIZE = 4; // 定义计算到多大，需要重新进行添加任务

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


    // 计算池
    private HashMap<String, ComputePool> computePool = new HashMap<>();

    public ComputeDispenseServiceImpl(ComputeResultHandler computeResultHandler
            ,ComputeResultMapper computeResultMapper){
        this.computeResultMapper=computeResultMapper;
        this.computeResultHandler=computeResultHandler;
        fillJob();//初始化分配任务
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
        if (computePool.containsKey(tokenId)){ // 计算池中存在当前客户端
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
        // 从计算结果表中找到最后一位
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
