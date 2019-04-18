package gq.dengbo.oa.dao;

import gq.dengbo.oa.entity.ClaimVoucherItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *报销单条目
 * 增删改
 */
@Repository("claimVoucherItemDao")
public interface ClaimVoucherItemDao {

    void insert(ClaimVoucherItem claimVoucherItem);
    void update(ClaimVoucherItem claimVoucherItem);
    void delete(int id);
    //根据报销单编号查询报销单条目
    List<ClaimVoucherItem> selectByClaimVoucher(int cvid);
}
