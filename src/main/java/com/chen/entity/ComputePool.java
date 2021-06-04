package com.chen.entity;

import lombok.Data;

import java.util.ArrayDeque;

@Data
public class ComputePool {
    private String token;
    private ArrayDeque<Long> bitPool;
}
