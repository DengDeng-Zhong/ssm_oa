package gq.dengbo.oa.dao;

import gq.dengbo.oa.entity.ClaimVoucher;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 报销单接口
 * 增删改查
 */
@Repository("claimVoucherDao")
public interface ClaimVoucherDao {

    void insert(ClaimVoucher claimVoucher);

    void update(ClaimVoucher claimVoucher);

    void delete(int id);

    ClaimVoucher select(int id);
    //根据创建者编号获取多个报销单
    List<ClaimVoucher> selectByCreateSn(String sn);
    //根据待处理人编号获取多个报销单
    List<ClaimVoucher> selectByNextDealSn(String ndsn);



}
