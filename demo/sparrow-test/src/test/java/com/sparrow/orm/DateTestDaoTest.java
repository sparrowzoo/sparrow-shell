package com.sparrow.orm;

import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.orm.dao.DateTestDao;
import com.sparrow.orm.dao.impl.UserDaoImpl;
import com.sparrow.orm.po.DateTest;
import org.junit.Test;

import java.util.Date;

/**
 * Created by harry on 2018/2/6.
 */
public class DateTestDaoTest {

    @Test
    public void userTest() {
        Container container = ApplicationContext.getContainer();
        ContainerBuilder builder = new ContainerBuilder().contextConfigLocation("/dao.xml");
        container.init(builder);
        DateTestDao dateTestDao = container.getBean("dateTestDao");

        DateTest dateTest = new DateTest();
        dateTest.setBirthday(new Date());
        dateTest.setDt(new Date());
        dateTestDao.insert(dateTest);

        dateTest = dateTestDao.getEntity(1L);
        System.out.println(dateTest.getBirthday());

        System.out.println(dateTest.getDt());
        System.out.println(dateTest.getDt().toInstant());
        java.sql.Date date = new java.sql.Date(System.currentTimeMillis());
        System.out.println(date.toInstant());
    }
}
