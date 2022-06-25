package com.sparrow.orm;

import com.sparrow.constant.DateTime;
import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import com.sparrow.enums.Gender;
import com.sparrow.orm.dao.UserDAO;
import com.sparrow.orm.po.User;
import com.sparrow.protocol.enums.StatusRecord;
import com.sparrow.transaction.Transaction;
import com.sparrow.transaction.TransactionManager;
import com.sparrow.utility.DateTimeUtility;

import java.sql.Date;

public class TransactionManagerTest {
    public static void main(String[] args) {
        Container container = ApplicationContext.getContainer();
        container.setContextConfigLocation("/dao.xml");
        container.init();
        UserDAO userDAO = container.getBean("userDao");
        TransactionManager transactionManager = container.getBean("transactionManager");
        transactionManager.start(new Transaction<String>() {
            @Override
            public String execute() throws Exception {
                User user = new User();
                user.setUserName("harry");
                user.setBirthday(new Date(DateTimeUtility.parse("2018-02-06", DateTime.FORMAT_YYYY_MM_DD)));
                user.setAvatar("http://www.sparrowzoo.com/avator.jpg");
                user.setCent(10L);
                user.setPassword("123455");
                user.setNickName("nickName");
                user.setUpdateTime(System.currentTimeMillis());
                user.setCreateTime(System.currentTimeMillis());
                user.setSex((byte) Gender.FEMALE.ordinal());
                user.setStatus((byte) StatusRecord.ENABLE.ordinal());
                Long id = userDAO.insert(user);
                User user1 = userDAO.getEntity(id);
                userDAO.delete(id);
                return user1.getUserName();
            }
        }, "user_default");
    }
}
