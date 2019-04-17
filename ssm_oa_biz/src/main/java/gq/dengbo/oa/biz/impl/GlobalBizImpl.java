package gq.dengbo.oa.biz.impl;

import gq.dengbo.oa.biz.GlobalBiz;
import gq.dengbo.oa.dao.EmployeeDao;
import gq.dengbo.oa.entity.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("globalBiz")
public class GlobalBizImpl implements GlobalBiz {

    @Autowired
    private EmployeeDao employeeDao;

    public Employee login(String sn, String password) {
        Employee employee = employeeDao.select(sn);
        //如果根据sn有查到的员工并且密码正确
        if (employee!=null&&employee.getPassword().equals(password)){
            return employee;
        }
        return null;
    }

    public void changePassword(Employee employee) {
        employeeDao.update(employee);
    }
}
