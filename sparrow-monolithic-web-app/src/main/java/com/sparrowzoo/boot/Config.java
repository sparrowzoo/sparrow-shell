package com.sparrowzoo.boot;

import com.sparrow.chat.boot.config.EnableChatApp;
import com.sparrow.file.config.EnableFileApp;
import com.sparrow.passport.config.EnablePassport;
import com.sparrowzoo.coder.boot.config.EnableCoderApp;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFileApp
@EnablePassport
@EnableCoderApp
@EnableChatApp
public class Config {
}
