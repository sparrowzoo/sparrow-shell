package com.sparrow.container;

import com.sparrow.protocol.POJO;
import lombok.Data;

/**
 * @author zhanglz
 */
@Data
public class User3 implements POJO {

    public User3(String user) {
        this.user = user;
    }

    private String user;
}
