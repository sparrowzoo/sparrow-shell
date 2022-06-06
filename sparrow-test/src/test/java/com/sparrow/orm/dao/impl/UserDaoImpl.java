package com.sparrow.orm.dao.impl;

import com.sparrow.orm.dao.UserDAO;
import com.sparrow.orm.po.User;
import com.sparrow.orm.query.BooleanCriteria;
import com.sparrow.orm.query.Criteria;
import com.sparrow.orm.query.SearchCriteria;
import com.sparrow.orm.template.impl.ORMStrategy;

import java.util.List;

/**
 * Created by harry on 2018/2/6.
 */
public class UserDaoImpl extends ORMStrategy<User, Long> implements UserDAO {
    @Override
    public List<User> query(String userName, String nickName, Integer sex, Integer status) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setWhere(BooleanCriteria.criteria(
                Criteria.field("user.userName").equal(userName)).
                and(Criteria.field("user.nickName").equal(nickName))
                .or(BooleanCriteria.criteria(Criteria.field("user.userId").equal(2L)).or(Criteria.field("user.nickName").equal(nickName))));
        return this.getList(searchCriteria);
    }
}
