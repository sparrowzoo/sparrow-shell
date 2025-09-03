package com.ideaworks.club.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ideaworks.club.bean.UserBean;
import com.ideaworks.club.dao.DataSource;

@Component
public class UserService {

    public UserBean getUser(String username) {
        // If no such user return null
        if (!DataSource.getData().containsKey(username))
            return null;
        UserBean user = new UserBean();
        Map<String, String> detail = DataSource.getData().get(username);
        user.setUsername(username);
        user.setPassword(detail.get("password"));
        user.setRole(detail.get("role"));
        user.setPermission(detail.get("permission"));
        return user;
    }

}
