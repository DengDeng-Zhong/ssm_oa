package gq.dengbo.oa.biz;

import gq.dengbo.oa.entity.Employee;

public interface GlobalBiz {

    Employee login(String sn, String password);

    void changePassword(Employee employee);

}
