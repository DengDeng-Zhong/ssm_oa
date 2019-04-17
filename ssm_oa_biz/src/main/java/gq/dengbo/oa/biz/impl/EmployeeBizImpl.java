package gq.dengbo.oa.biz.impl;


import gq.dengbo.oa.biz.EmployeeBiz;
import gq.dengbo.oa.dao.EmployeeDao;
import gq.dengbo.oa.entity.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("employeeBiz")
public class EmployeeBizImpl implements EmployeeBiz {
//    private static Logger logger = (Logger) LoggerFactory.getLogger(EmployeeBizImpl.class);
    @Autowired
    private EmployeeDao employeeDao;

    public void add(Employee employee) {
        employeeDao.insert(employee);
    }

    public void edit(Employee employee) {
//        logger.info("Enter edit()");
        employee.setPassword("000000");
        employeeDao.update(employee);
    }

    public void remove(String sn) {
        employeeDao.delete(sn);
    }

    public Employee get(String sn) {
        return employeeDao.select(sn);
    }

    public List<Employee> getAll() {
        return employeeDao.selectAll();
    }
}
