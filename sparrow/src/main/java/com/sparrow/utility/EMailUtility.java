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

package com.sparrow.utility;

import com.sparrow.constant.Config;
import com.sparrow.constant.ConfigKeyLanguage;
import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Properties;

public class EMailUtility {
    private static Logger logger = LoggerFactory.getLogger(EMailUtility.class);
    private String localAddress;
    private String host;
    private String from;
    private String to;
    private String subject;
    private String content;
    private boolean authentication = true;
    private String username;
    private String password;
    private String webSitName;

    public String getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String localAddress) {
        this.localAddress = localAddress;
    }

    public String getWebSitName() {
        return webSitName;
    }

    public void setWebSitName(String webSitName) {
        this.webSitName = webSitName;
    }

    public Boolean sendMail(String toAddress, String subject, String content,
                            String language) throws BusinessException {
        String websiteName = ConfigUtility.getLanguageValue(
                ConfigKeyLanguage.WEBSITE_NAME, language);

        this.setHost(ConfigUtility.getValue(Config.EMAIL_HOST));
        this.setFrom(ConfigUtility.getValue(Config.EMAIL_FROM));
        this.setUsername(ConfigUtility.getValue(Config.EMAIL_USERNAME));

        String emailPassword = System.getenv(Config.EMAIL_PASSWORD);
        if (StringUtility.isNullOrEmpty(emailPassword)) {
            emailPassword = ConfigUtility.getValue(Config.EMAIL_PASSWORD);
        }
        this.setPassword(emailPassword);
        this.setLocalAddress(ConfigUtility.getValue(Config.EMAIL_LOCAL_ADDRESS));
        this.setAuthentication(true);
        if (content.contains("$website_name")) {
            content = content.replace("$website_name", websiteName);
        }

        if (subject.contains("$website_name")) {
            subject = subject.replace("$website_name", websiteName);
        }
        this.setTo(toAddress);
        this.setSubject(subject);
        this.setContent(content);
        this.setWebSitName(websiteName);
        logger.info("to {} from {},host {},local:{},user-name:{},email password is {}",
                this.getTo(),
                this.getFrom(),
                this.getHost(),
                this.getLocalAddress(),
                this.getUsername(),
                emailPassword);

        return this.sendMail();
    }

    /**
     * 腾迅邮件需要手动开启smtp服务
     */
    private Boolean sendMail() throws BusinessException {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.localhost", this.getLocalAddress());
            props.put("mail.smtp.host",this.getHost());
            props.put("mail.smtp.port",465);
            props.put("mail.smtp.protocol","smtp");
            props.put("mail.smtp.ssl.enable","true");
            if (!authentication) {
                props.put("mail.smtp.auth", "false");
            } else {
                props.put("mail.smtp.auth", "true");
            }
            final String finalEmailUserName = this.getUsername();
            final String finalEmailPassword = this.getPassword();
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(finalEmailUserName,
                            finalEmailPassword);
                }
            });
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            InternetAddress fromAddress = new InternetAddress();
            fromAddress.setAddress(this.getFrom());
            fromAddress.setPersonal(this.getWebSitName());
            message.setFrom(fromAddress);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(
                    this.getTo()));
            message.setSubject(this.getSubject());
            message.setContent(this.getContent(), "text/html;charset=gb2312");
            message.saveChanges();
            if (authentication) {
                Transport transport = null;
                try {
                    transport = session.getTransport("smtp");
                    transport.connect(this.getHost(), this.getUsername(),
                            this.getPassword());
                    logger.info("host:{},username:{},password:{}", this.getHost()
                            , this.getUsername(), this.getPassword());
                    transport.sendMessage(message, message.getAllRecipients());
                } catch (Exception e) {
                    logger.error("send email error", e);
                    throw e;
                } finally {
                    if (transport != null) {
                        transport.close();
                    }
                }
            } else {
                Transport.send(message);
            }
            return true;
        } catch (Exception e) {
            logger.error("send email error", e);
            throw new BusinessException(SparrowError.GLOBAL_EMAIL_SEND_FAIL, Collections.singletonList((Object) this.getTo()));
        }
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isAuthentication() {
        return authentication;
    }

    public void setAuthentication(boolean authentication) {
        this.authentication = authentication;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
