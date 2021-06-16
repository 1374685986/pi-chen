package com.chen.dao;

import com.chen.entity.ComputeResultBit;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ComputeResultBitMapper {
    // 查找最近的10个
    List<ComputeResultBit> findTop10ByCheckedOrderByComputeTime(Boolean checked);

    // 找位数最大的一位
    ComputeResultBit findTopByDigit(Long digit);

    // 查询某个client的计算结果
    List<ComputeResultBit> findNeedChecked(String token);

    int save(ComputeResultBit bit);


    int update(ComputeResultBit bit);
}
