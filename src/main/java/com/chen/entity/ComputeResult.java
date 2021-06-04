package com.chen.entity;

import lombok.Data;

@Data

/*
* 计算结果实体类
* */
public class ComputeResult extends BaseEntity{

    // 计算结果
    private String result;

    // 从第几位开始
    private Long startIndex;

    // 从第几位结束
    private Long endIndex;

    // 长度
    private Long length;
}
