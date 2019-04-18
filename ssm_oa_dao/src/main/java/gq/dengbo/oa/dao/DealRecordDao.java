package gq.dengbo.oa.dao;

import gq.dengbo.oa.entity.DealRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理记录
 * 不能删,更不能改
 */
@Repository("dealRecordDao")
public interface DealRecordDao {
    void insert(DealRecord dealRecord);
    //查询报销单处理流程
    List<DealRecord> selectByClaimVoucher(int cvid);
}
