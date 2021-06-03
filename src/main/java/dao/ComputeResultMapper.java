package dao;

import entity.ComputeRecord;
import entity.ComputeResult;
import entity.ComputeResultBit;
import entity.vo.ComputeJobResult;
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
