package com.sparrow.orm;

import com.sparrow.container.Container;
import com.sparrow.core.spi.ApplicationContext;
import org.junit.Test;

/**
 * Created by harry on 2018/2/6.
 */
public class JDBCTemplateTest {
    @Test
    public void initStructure() {
        Container container = ApplicationContext.getContainer();
        container.setContextConfigLocation("/dao.xml");
        container.init();
        JDBCSupport jdbcSupport = JDBCTemplate.getInstance("user",null);
        int affectCount= jdbcSupport.executeUpdate("DROP TABLE IF EXISTS `user`;");
        affectCount=jdbcSupport.executeUpdate("CREATE TABLE `user` (\n" +
                "  `user_id` bigint(11) NOT NULL AUTO_INCREMENT,\n" +
                "  `nick_name` varchar(20) NOT NULL DEFAULT '',\n" +
                "  `user_name` varchar(8) NOT NULL DEFAULT '',\n" +
                "  `password` varchar(50) NOT NULL DEFAULT '',\n" +
                "  `avatar` varchar(200) NOT NULL,\n" +
                "  `sex` tinyint(2) unsigned NOT NULL DEFAULT '0',\n" +
                "  `birthday` date NOT NULL,\n" +
                "  `cent` bigint(10) NOT NULL DEFAULT '0',\n" +
                "  `create_time` bigint(10) NULL,\n" +
                "  `update_time` bigint(10) NULL DEFAULT NULL,\n" +
                "  `status` tinyint(3) NOT NULL DEFAULT '0',\n" +
                "  PRIMARY KEY (`user_id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8;");
        System.out.println(affectCount);
    }
}
