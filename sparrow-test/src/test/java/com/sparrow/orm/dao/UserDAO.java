package com.sparrow.orm.dao;

import com.sparrow.orm.po.User;
import java.util.List;
import org.springframework.dao.support.DaoSupport;

/**
 * Created by harry on 2018/2/6.
 */
public interface UserDAO  extends DaoSupport<User,Long> {
    List<User> query(String userName, String nickName, Integer sex, Integer status);
}
