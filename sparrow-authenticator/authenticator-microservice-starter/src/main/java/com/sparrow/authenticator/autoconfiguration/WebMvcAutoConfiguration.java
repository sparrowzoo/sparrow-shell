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

package com.sparrow.authenticator.autoconfiguration;

import com.sparrow.authenticator.AuthenticatorConfigReader;
import com.sparrow.authenticator.filter.MicroServiceFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

public class WebMvcAutoConfiguration implements WebMvcConfigurer {
    @Autowired
    private AuthenticatorConfigReader configReader;

    @Bean
    @ConditionalOnMissingBean(MicroServiceFilter.class)
    public MicroServiceFilter microServiceFilter() {
        return new MicroServiceFilter(this.configReader);
    }


    @Bean
    public FilterRegistrationBean<Filter> microServiceFilterBean() {
        FilterRegistrationBean<Filter> microServiceFilterBean = new FilterRegistrationBean();
        microServiceFilterBean.setFilter(microServiceFilter());
        microServiceFilterBean.addUrlPatterns("/*");
        microServiceFilterBean.setName("microServiceFilter");
        //推荐在filterbean 中设置order
        microServiceFilterBean.setOrder(1);
        //多个filter的时候order的数值越小 则优先级越高
        return microServiceFilterBean;
    }
}
