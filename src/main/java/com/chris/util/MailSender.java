package com.chris.util;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

/**
 * Created by YaoQi on 2017/2/24.
 */
@Service
public class MailSender implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    //private JavaMailSenderImpl mailSender;
    private MimeMessage mimeMessage;

    @Autowired
    private VelocityEngine velocityEngine;

    @Override
    public void afterPropertiesSet() throws Exception {

        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.host", "smtp.qq.com");
        javaMailProperties.put("mail.smtp.socketFactory.port", "465");
        javaMailProperties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        javaMailProperties.put("mail.smtp.auth", "true");
        javaMailProperties.put("mail.smtp.port", "25");
        Session session = Session.getDefaultInstance(javaMailProperties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("1978151299@qq.com", "xxxx");
                    }
                });
        mimeMessage = new MimeMessage(session);

    }

    public boolean sendWithHTMLTemplate(String to, String subject, String template, Map<String, Object> model) {
        try {
            //设置发送邮件的信息，包括模版编程字符串
            String nick = MimeUtility.encodeText("nowcode course");
            InternetAddress from = new InternetAddress(nick + "<1978151299@qq.com>");
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            String result = VelocityEngineUtils
                    .mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setFrom(from);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(result);
            Transport.send(mimeMessage);
            return true;
        } catch (Exception e) {
            logger.error("发送邮件失败" + e.getMessage());
            return false;
        }
    }
}
