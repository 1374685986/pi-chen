package com.chen.entity;

import lombok.Data;

import java.util.Date;

@Data

/*
* 计算记录实体类
* */
public class ComputeRecord extends BaseEntity{
    private Date computeDate;

    private String token;

    private Integer result;
}
