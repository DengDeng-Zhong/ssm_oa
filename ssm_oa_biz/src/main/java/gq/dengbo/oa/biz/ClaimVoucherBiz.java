package gq.dengbo.oa.biz;

import gq.dengbo.oa.entity.ClaimVoucher;
import gq.dengbo.oa.entity.ClaimVoucherItem;
import gq.dengbo.oa.entity.DealRecord;

import java.util.List;

public interface ClaimVoucherBiz {

    /**
     * 保存报销单
     * @param claimVoucher 表单对象(里面只包含基本信息)
     * @param items 报销单的条目集合
     */
    void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items);
    /**
     * 获取报销单对象
     * @param id 报销单编号
     * @return
     */
    ClaimVoucher get(int id);
    /**
     * 获取报销单对于的所有的条目
     * @param cvid 报销单编号
     * @return
     */
    List<ClaimVoucherItem> getItems(int cvid);
    /**
     * 审核记录
     * @param cvid 报销单编号
     * @return
     */
    List<DealRecord> getRecords(int cvid);


    /**
     * 获取个人报销单
     * @param sn 员工编号
     * @return
     */
    List<ClaimVoucher> getForSelf(String sn);
    /**
     * 获取待处理报销单
     * @param sn 员工编号
     * @return
     */
    List<ClaimVoucher> getForDeal(String sn);

    /**
     * 更新报销单
     * @param claimVoucher 报销单对象
     * @param items 报销条目集合
     */
    void update(ClaimVoucher claimVoucher,List<ClaimVoucherItem> items);

    /**
     * 提交
     * @param id
     */
    void submit(int id);

    /**
     * 审核报销单,打款
     * @param dealRecord
     */
    void deal(DealRecord dealRecord);
}
