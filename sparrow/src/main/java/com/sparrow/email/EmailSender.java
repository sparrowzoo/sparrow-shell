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

package com.sparrow.email;

import com.sparrow.protocol.BusinessException;
import com.sparrow.protocol.constant.SparrowError;
import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class EmailSender {
    protected String localAddress;
    protected String host;
    protected String from;
    protected boolean authentication = true;
    protected String username;
    protected String password;

    public EmailSender() {
    }

    public EmailSender(String localAddress, String host, String from, String username, String password) {
        this.localAddress = localAddress;
        this.host = host;
        this.from = from;
        this.username = username;
        this.password = password;
    }

    public void send(String fromName, String to, String subject, String content) throws BusinessException {
        try {
            Properties props = System.getProperties();
            props.put("mail.smtp.localhost", this.localAddress);
            props.put("mail.smtp.host", this.host);
            props.put("mail.smtp.port", 465);
            props.put("mail.smtp.protocol", "smtp");
            props.put("mail.smtp.ssl.enable", "true");
            if (!authentication) {
                props.put("mail.smtp.auth", "false");
            } else {
                props.put("mail.smtp.auth", "true");
            }
            final String finalEmailUserName = this.username;
            final String finalEmailPassword = this.password;
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(finalEmailUserName, finalEmailPassword);
                }
            });
            session.setDebug(true);
            MimeMessage message = new MimeMessage(session);
            InternetAddress fromAddress = new InternetAddress();
            fromAddress.setAddress(this.from);
            fromAddress.setPersonal(fromName);
            message.setFrom(fromAddress);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            if (subject.contains("$website_name")) {
                subject = subject.replace("$website_name", fromName);
            }
            message.setSubject(subject);

            if (content.contains("$website_name")) {
                content = content.replace("$website_name", fromName);
            }



            message.setContent(content, "text/html;charset=gb2312");
            message.saveChanges();
            if (authentication) {
                Transport transport = null;
                try {
                    transport = session.getTransport("smtp");
                    transport.connect(this.host, this.username, this.password);
                    log.info("host:{},username:{},password:{}", this.host, this.username, this.password);
                    transport.sendMessage(message, message.getAllRecipients());
                } catch (Exception e) {
                    log.error("send email error", e);
                    throw e;
                } finally {
                    if (transport != null) {
                        transport.close();
                    }
                }
            } else {
                Transport.send(message);
            }
        } catch (Exception e) {
            log.error("send email error", e);
            throw new BusinessException(SparrowError.GLOBAL_EMAIL_SEND_FAIL, Collections.singletonList(to));
        }
    }
}
