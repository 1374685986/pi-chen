package com.chen.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ComputeInfo {
    // 修改最大的bit
    int updateMaxBit(Long bits);

    //查找最大bit
    Long queryMaxBit();
}
