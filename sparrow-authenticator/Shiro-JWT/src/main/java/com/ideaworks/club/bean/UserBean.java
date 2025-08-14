package com.ideaworks.club.bean;

import lombok.Data;

/**
 * 用户实体 Bean
 * @author 王庆港
 * @version 1.0.0
*/
@Data
public class UserBean {
	private String username;
    private String password;
    private String role;
    private String permission;
}
