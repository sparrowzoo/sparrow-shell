package com.sparrow.orm;

import com.sparrow.constant.DateTime;
import com.sparrow.container.Container;
import com.sparrow.container.ContainerBuilder;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.enums.Gender;
import com.sparrow.orm.dao.UserDAO;
import com.sparrow.orm.dao.impl.UserDaoImpl;
import com.sparrow.orm.po.User;
import com.sparrow.protocol.dao.UniqueKeyCriteria;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.utility.DateTimeUtility;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

/**
 * Created by harry on 2018/2/6.
 */
public class UserDaoTest {
    /**
     * 有一个数据库任务 大概的数据有几万条
     * 这个同学已经离职不在了
     *
     * 这个项目直接交到你手里
     * 只有代码
     * 问题：
     * 本地跑起来数据insert 是正常的（没有异常报错）
     * 现象：你在数据库里看不到数据
     *
     * 1. 数据源问题确认是否正确
     * 2. 确认我其他的表，连当前数据源是否正确写入
     * 3. 事务的问题
     */
    @Test
    public void getUser() {
        Container container = ApplicationContext.getContainer();
        //container.setContextConfigLocation("/dao.xml");
        container.init(new ContainerBuilder());
        UserDaoImpl userDAO = container.getBean("userDao");
        userDAO.getByUserName("harry");

        java.util.Date date=new java.util.Date();
        java.sql.Date date1=new java.sql.Date(System.currentTimeMillis());

        System.out.println(date.toInstant());
        System.out.println(date1.toInstant());


    }

    @Test
    public void userTest() {
        Container container = ApplicationContext.getContainer();
        ContainerBuilder builder= new ContainerBuilder().contextConfigLocation("/dao.xml");
        container.init(builder);
        UserDAO userDAO = container.getBean("userDao");

        User user = new User();
        user.setUserName("harry");
        user.setBirthday(new Date(DateTimeUtility.parse("2018-02-06", DateTime.FORMAT_YYYY_MM_DD)));
        user.setAvatar("http://www.sparrowzoo.com/avator.jpg");
        user.setCent(10L);
        user.setPassword("123455");
        user.setNickName("nickName");
        user.setUpdateTime(System.currentTimeMillis());
        user.setCreateTime(System.currentTimeMillis());
        user.setGender(Gender.FEMALE.ordinal());
        user.setStatus((byte) StatusRecord.ENABLE.ordinal());
        Long id = userDAO.insert(user);

        user = userDAO.getEntity(id);
        System.out.println(user.getAvatar());

        List<User> userList = userDAO.query("harry", "nickName", 1, 1);
        while (true) {
            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
            }
        }
    }
}
