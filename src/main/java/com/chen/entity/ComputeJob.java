package com.chen.entity;

import com.chen.entity.vo.ComputeJobResult;
import lombok.Data;

import java.util.List;

@Data
public class ComputeJob {
    private Long bit;
    private List<ComputeJobResult> results;
}
