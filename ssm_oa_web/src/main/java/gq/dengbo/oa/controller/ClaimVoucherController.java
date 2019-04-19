package gq.dengbo.oa.controller;

import gq.dengbo.oa.biz.ClaimVoucherBiz;
import gq.dengbo.oa.dto.ClaimVoucherInfo;
import gq.dengbo.oa.entity.DealRecord;
import gq.dengbo.oa.entity.Employee;
import gq.dengbo.oa.global.Contant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;
@Controller("claimVoucherController")
@RequestMapping("/claim_voucher")
public class ClaimVoucherController {
    @Autowired
    private ClaimVoucherBiz claimVoucherBiz;
    @RequestMapping("/to_add")
    public String toAdd(Map<String,Object> map){
        map.put("items", Contant.getItems());
        map.put("info",new ClaimVoucherInfo());

        return "claim_voucher_add";
    }

    @RequestMapping("/add")
    public String add(HttpSession session, ClaimVoucherInfo info){
        Employee employee = (Employee) session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherBiz.save(info.getClaimVoucher(),info.getItems());
        return "redirect:deal";
    }

    /**
     *
     * @param id 从上一个页面携带的
     * @param map
     * @return
     */
    @RequestMapping("/detail")
    public String detail(int id, Map<String,Object> map){
        map.put("claimVoucher",claimVoucherBiz.get(id));
        map.put("items",claimVoucherBiz.getItems(id));
        map.put("records",claimVoucherBiz.getRecords(id));
        return "claim_voucher_detail";
    }

    @RequestMapping("/self")
    public String self(HttpSession session, Map<String,Object> map){
        /*
        1.获取当前用户
        2.根据当期用户的编号获取用户本身创建的报销单
        3.放入map中
         */
        Employee employee = (Employee) session.getAttribute("employee");
        map.put("list",claimVoucherBiz.getForSelf(employee.getSn()));
        return "claim_voucher_self";
    }
    @RequestMapping("/deal")
    public String deal(HttpSession session, Map<String,Object> map){
        Employee employee = (Employee) session.getAttribute("employee");
        map.put("list",claimVoucherBiz.getForDeal(employee.getSn()));
        return "claim_voucher_deal";
    }

    /**
     * 去修改报销单
     * @param id 报销单编号
     * @param map
     * @return
     */
    @RequestMapping("/to_update")
    public String toUpdate(int id,Map<String,Object> map){
        map.put("items", Contant.getItems());
        ClaimVoucherInfo info = new ClaimVoucherInfo();
        info.setClaimVoucher(claimVoucherBiz.get(id));
        info.setItems(claimVoucherBiz.getItems(id));
        map.put("info",info);

        return "claim_voucher_update";
    }

    @RequestMapping("/update")
    public String update(HttpSession session, ClaimVoucherInfo info){
        Employee employee = (Employee) session.getAttribute("employee");
        info.getClaimVoucher().setCreateSn(employee.getSn());
        claimVoucherBiz.update(info.getClaimVoucher(),info.getItems());
        return "redirect:deal";
    }

    @RequestMapping("/submit")
    public String submit(int id){
        claimVoucherBiz.submit(id);

        return "redirect:deal";
    }

    @RequestMapping("/to_check")
    public String toCheck(int id,Map<String,Object> map){
        map.put("claimVoucher",claimVoucherBiz.get(id));
        map.put("items",claimVoucherBiz.getItems(id));
        map.put("records",claimVoucherBiz.getRecords(id));
        DealRecord dealRecord = new DealRecord();
        dealRecord.setClaimVoucherId(id);
        map.put("record",dealRecord);

        return "claim_voucher_check";
    }

    @RequestMapping("/check")
    public String check(HttpSession session, DealRecord dealRecord){
        Employee employee = (Employee) session.getAttribute("employee");
        dealRecord.setDealSn(employee.getSn());

        claimVoucherBiz.deal(dealRecord);
        return "redirect:deal";
    }

}
