package gq.dengbo.oa;

import gq.dengbo.oa.dao.EmployeeDao;
import gq.dengbo.oa.entity.Employee;
import gq.dengbo.oa.global.Contant;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class TestSelectByDepartmentAndPost {
    final static Logger logger = LoggerFactory.getLogger(TestSelectByDepartmentAndPost.class);
    @Autowired
    private EmployeeDao employeeDao;

    @Test
    public void testByDP(){
        logger.info("test");
        logger.warn("warn");
//        System.out.println("=====");
//        List<Employee> employees = employeeDao.selectByDepartmentAndPost("10005", Contant.POST_FM);
//        System.out.println(employees.size());
//        for (Employee employee : employees) {
//            System.out.println(employee.getSn());
//        }


    }
}
