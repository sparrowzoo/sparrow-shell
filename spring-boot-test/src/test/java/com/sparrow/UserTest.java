/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sparrow;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sparrow.mapper.UserMapper;
import com.sparrow.po.User;
import com.sparrow.po.UserAddress;
import icu.mhb.mybatisplus.plugln.core.JoinLambdaWrapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    public void test() {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getId, 1);
        System.out.println(wrapper.getSqlSegment());
        this.userMapper.selectOne(wrapper);
    }

    @Test
    public void joinTest() {
        JoinLambdaWrapper<User> wrapper =new JoinLambdaWrapper<>(User.class);

        wrapper.leftJoin(User.class, User::getId, UserAddress::getUserId);
        this.userMapper.selectList(wrapper);
    }
}
