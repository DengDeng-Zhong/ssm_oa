package gq.dengbo.oa.dto;

import gq.dengbo.oa.entity.ClaimVoucher;
import gq.dengbo.oa.entity.ClaimVoucherItem;

import java.util.List;

/**
 * 收集用户提交的数据
 */
public class ClaimVoucherInfo {
    private ClaimVoucher claimVoucher;
    private List<ClaimVoucherItem> items;


    public ClaimVoucher getClaimVoucher() {
        return claimVoucher;
    }

    public void setClaimVoucher(ClaimVoucher claimVoucher) {
        this.claimVoucher = claimVoucher;
    }

    public List<ClaimVoucherItem> getItems() {
        return items;
    }

    public void setItems(List<ClaimVoucherItem> items) {
        this.items = items;
    }
}
