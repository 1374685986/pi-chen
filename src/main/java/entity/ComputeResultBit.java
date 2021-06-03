package entity;

import lombok.Data;

import java.util.Date;
import java.util.List;


/*
* 计算过程记录
* */
@Data
public class ComputeResultBit extends BaseEntity{
    // 计算时间
    private Date computeTime;

    // 是否检查（是否有重复执行了）
    private Boolean checked = true;

    // 第多少位
    private Long digit;

    // 计算结果
    private Integer result;

    // 计算记录
    private List<ComputeRecord> records;
}
