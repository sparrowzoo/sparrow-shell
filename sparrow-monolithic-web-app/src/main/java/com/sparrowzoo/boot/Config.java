package com.sparrowzoo.boot;

import com.sparrow.chat.boot.config.EnableChatWebMvc;
import com.sparrow.file.config.EnableFileWebMvc;
import com.sparrow.passport.config.EnablePassportWebMvc;
import com.sparrowzoo.coder.boot.config.EnableCoderWebMvc;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@Configuration
//@ComponentScan(
//        basePackages = {"com.sparrow.file"}
//)
//@ComponentScan(
//        basePackages = {"com.sparrow.passport"}
//)
@EnableFileWebMvc
@EnablePassportWebMvc
@EnableSwagger2WebMvc
public class Config {
}
