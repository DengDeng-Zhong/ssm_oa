package gq.dengbo.oa.biz.impl;

import gq.dengbo.oa.biz.ClaimVoucherBiz;
import gq.dengbo.oa.dao.ClaimVoucherDao;
import gq.dengbo.oa.dao.ClaimVoucherItemDao;
import gq.dengbo.oa.dao.DealRecordDao;
import gq.dengbo.oa.entity.ClaimVoucher;
import gq.dengbo.oa.entity.ClaimVoucherItem;
import gq.dengbo.oa.entity.DealRecord;
import gq.dengbo.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {

    //持久化操作对象
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;

    public void save(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        //业务规则
        claimVoucher.setCreateTime(new Date());
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());//将待处理人设置成创建者
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);
        claimVoucherDao.insert(claimVoucher);

        for (ClaimVoucherItem item : items) {
                item.setClaimVoucherId(claimVoucher.getId());
                claimVoucherItemDao.insert(item);
        }

    }

    public ClaimVoucher get(int id) {
        return claimVoucherDao.select(id);
    }

    public List<ClaimVoucherItem> getItems(int cvid) {
        return claimVoucherItemDao.selectByClaimVoucher(cvid);
    }

    public List<DealRecord> getRecords(int cvid) {
        return dealRecordDao.selectByClaimVoucher(cvid);
    }
}
