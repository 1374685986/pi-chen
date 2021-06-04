package com.chen.dao;

import com.chen.entity.ComputeRecord;
import com.chen.entity.ComputeResult;
import com.chen.entity.ComputeResultBit;
import com.chen.entity.vo.ComputeJobResult;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComputeResultMapper {
    // 保存计算结果
    int saveResults(ComputeJobResult computeJobResult);

    // 保存计算记录
    int saveRecords(ComputeRecord computeRecord);

    // 保存计算结果的位数
    int saveResultBit(ComputeResultBit computeResultBit);

    // 查找最后的位数
    ComputeResult findTopByOrderByEndIndexDesc();

}
