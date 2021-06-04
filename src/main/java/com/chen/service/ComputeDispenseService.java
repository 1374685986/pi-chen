package com.chen.service;

import com.chen.entity.vo.ComputeJobDto;
import com.chen.entity.vo.ComputeJobResult;
import lombok.Synchronized;

public interface ComputeDispenseService {
    // 分发任务
    ComputeJobDto dispense();

    //初始化
    void init(String token);

    //分发任务（带token） 指定分发
    ComputeJobDto dispense(String tokenId);

    // 记录
    void reclaim(ComputeJobResult result);

    // 获取bit
    Long getBit();

    // 获取任务
    ComputeJobDto getJob(String token);

    // 添加任务
    void fillJob();
}
