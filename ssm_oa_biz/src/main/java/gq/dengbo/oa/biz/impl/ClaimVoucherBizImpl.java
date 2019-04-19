package gq.dengbo.oa.biz.impl;

import gq.dengbo.oa.biz.ClaimVoucherBiz;
import gq.dengbo.oa.dao.ClaimVoucherDao;
import gq.dengbo.oa.dao.ClaimVoucherItemDao;
import gq.dengbo.oa.dao.DealRecordDao;
import gq.dengbo.oa.dao.EmployeeDao;
import gq.dengbo.oa.entity.ClaimVoucher;
import gq.dengbo.oa.entity.ClaimVoucherItem;
import gq.dengbo.oa.entity.DealRecord;
import gq.dengbo.oa.entity.Employee;
import gq.dengbo.oa.global.Contant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service("claimVoucherBiz")
public class ClaimVoucherBizImpl implements ClaimVoucherBiz {
    final static Logger logger = LoggerFactory.getLogger(ClaimVoucherBizImpl.class);
    //持久化操作对象
    @Autowired
    private ClaimVoucherDao claimVoucherDao;
    @Autowired
    private ClaimVoucherItemDao claimVoucherItemDao;
    @Autowired
    private DealRecordDao dealRecordDao;
    @Autowired
    private EmployeeDao employeeDao;

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

    public List<ClaimVoucher> getForSelf(String sn) {
        return claimVoucherDao.selectByCreateSn(sn);
    }

    public List<ClaimVoucher> getForDeal(String sn) {
        return claimVoucherDao.selectByNextDealSn(sn);
    }

    @Override
    public void update(ClaimVoucher claimVoucher, List<ClaimVoucherItem> items) {
        //待处理人还是本身,状态修改为新建状态
        claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
        claimVoucher.setStatus(Contant.CLAIMVOUCHER_CREATED);

        claimVoucherDao.update(claimVoucher);

        /*
        更新条目集合
         */
        //获取当前的条目集合
        List<ClaimVoucherItem> olds = claimVoucherItemDao.selectByClaimVoucher(claimVoucher.getId());

        //判断当前获取的条目是否不在集合中
        for (ClaimVoucherItem old : olds) {
            boolean isHave = false;
            //迭代现有的准备更新的条目集合
            for (ClaimVoucherItem item : items) {
                //如果存在,不管
                if (item.getId()==old.getId()){
                    isHave = true;
                    break;
                }
            }
            //如果遍历完都没有发现的话,就将它删除
            if (!isHave){
                claimVoucherItemDao.delete(old.getId());
            }
        }
        for (ClaimVoucherItem item : items) {

            //为item设置报销单id,否则会报空指针
//            item.setClaimVoucherId(claimVoucher.getId());
//            if(item.getId()>0){
//                claimVoucherItemDao.update(item);
//            }else{
//                claimVoucherItemDao.insert(item);
//            }
            item.setClaimVoucherId(claimVoucher.getId());
            System.out.println(claimVoucher.getId());
            if (item.getId()>0&&item.getId()!=null){
                claimVoucherItemDao.update(item);
            }else{
                claimVoucherItemDao.insert(item);
            }
        }
    }

    @Override
    public void submit(int id) {
        /*
        对报销单进行处理,先拿到创建人
         */
        ClaimVoucher claimVoucher = claimVoucherDao.select(id);
        Employee employee = employeeDao.select(claimVoucher.getNextDealSn());

        claimVoucher.setStatus(Contant.CLAIMVOUCHER_SUBMIT);
        String sn = employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(), Contant.POST_FM).get(0).getSn();
        logger.info(sn);
        System.out.println("=====sn:"+sn);

        claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(employee.getDepartmentSn(),Contant.POST_FM).get(0).getSn());

        claimVoucherDao.update(claimVoucher);

        //记录的保存
        DealRecord dealRecord = new DealRecord();
        dealRecord.setDealWay(Contant.DEAL_SUBMIT);
        dealRecord.setDealSn(employee.getSn());
        dealRecord.setClaimVoucherId(id);
        dealRecord.setDealResult(Contant.CLAIMVOUCHER_SUBMIT);
        dealRecord.setDealTime(new Date());
        dealRecord.setComment("无");
        dealRecordDao.insert(dealRecord);
    }

    @Override
    public void deal(DealRecord dealRecord) {
        ClaimVoucher claimVoucher = claimVoucherDao.select(dealRecord.getClaimVoucherId());
        Employee employee = employeeDao.select(dealRecord.getDealSn());
        dealRecord.setDealTime(new Date());//修改业务处理时间
        if (dealRecord.getDealWay().equals(Contant.DEAL_PASS)){//通过
            //如果审核金额小于5000,并且审核人员是总经理则不需要复审
            if (claimVoucher.getTotalAmount()<=Contant.LIMIT_CHECK||employee.getPost().equals(Contant.POST_GM)){
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_APPROVED);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Contant.POST_CASHIER).get(0).getSn());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_APPROVED);
            }else{
                claimVoucher.setStatus(Contant.CLAIMVOUCHER_RECHECK);
                claimVoucher.setNextDealSn(employeeDao.selectByDepartmentAndPost(null,Contant.POST_GM).get(0).getSn());
                dealRecord.setDealResult(Contant.CLAIMVOUCHER_RECHECK);
            }
        }else if (dealRecord.getDealWay().equals(Contant.DEAL_BACK)){//打回
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_BACK);
            claimVoucher.setNextDealSn(claimVoucher.getCreateSn());
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_BACK);
        }else if (dealRecord.getDealWay().equals(Contant.DEAL_REJECT)){//拒绝
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_TERMINATED);
            claimVoucher.setNextDealSn(null);
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_TERMINATED);
        }else if (dealRecord.getDealWay().equals(Contant.DEAL_PAID)){//打款
            claimVoucher.setStatus(Contant.CLAIMVOUCHER_PAID);
            claimVoucher.setNextDealSn(null);
            dealRecord.setDealResult(Contant.CLAIMVOUCHER_PAID);
        }
        claimVoucherDao.update(claimVoucher);
        dealRecordDao.insert(dealRecord);

    }
}
