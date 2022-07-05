package com.sparrow.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.sparrow.constant.SparrowError;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.Result;

/**
 * @author: zhanglizhi01@meicai.cn
 * @date: 2019/6/12 10:54
 * @description:
 */
public class FastJsonTest {
    public static void main(String[] args) {

        User user=new User();
        user.setUserId("1");
        user.setUserName("userName");
        Result<User> result=Result.fail(new BusinessException(SparrowError.GLOBAL_DB_ADD_ERROR));

        String jsonString= JSON.toJSONString(result);
        System.out.println(JSON.toJSONString(result));

        result=JSON.parseObject(JSON.toJSONString(result),new TypeReference<Result<User>>(){});


        result= JSON.parseObject(jsonString,new TypeReference<Result<User>>(){});
        System.out.println(result.getData().getUserName());
    }
}
