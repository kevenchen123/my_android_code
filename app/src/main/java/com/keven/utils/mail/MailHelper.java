package com.keven.utils.mail;

import android.content.Context;
import java.util.Date;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailHelper {
    public static void startSendMail(Context context, final String str) {
        new Thread(new Runnable() {
            public void run() {
                MailSenderInfo appDefaultMainSenderInfo = MailHelper.getAppDefaultMainSenderInfo();
                appDefaultMainSenderInfo.setSubject("Subject");
                appDefaultMainSenderInfo.setContent(str);
                MailHelper.sendTextMail(appDefaultMainSenderInfo);
            }
        }).start();
    }

    public static MailSenderInfo getAppDefaultMainSenderInfo() {
        MailSenderInfo mailSenderInfo = new MailSenderInfo();
        mailSenderInfo.setMailServerHost("smtp.qq.com");
        mailSenderInfo.setMailServerPort("25");
        mailSenderInfo.setValidate(true);
        mailSenderInfo.setUserName("xx@qq.com");
        mailSenderInfo.setPassword("xxx");
        mailSenderInfo.setFromAddress("xx@qq.com");
        mailSenderInfo.setToAddress("xx@qq.com");
        return mailSenderInfo;
    }

    public static boolean sendTextMail(MailSenderInfo mailSenderInfo) {
        try {
            MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(mailSenderInfo.getProperties(), mailSenderInfo.isValidate() ? new MailAuthenticator(mailSenderInfo.getUserName(), mailSenderInfo.getPassword()) : null));
            mimeMessage.setFrom(new InternetAddress(mailSenderInfo.getFromAddress()));
            mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(mailSenderInfo.getToAddress()));
            mimeMessage.setSubject(mailSenderInfo.getSubject());
            mimeMessage.setSentDate(new Date());
            mimeMessage.setText(mailSenderInfo.getContent());
            Transport.send(mimeMessage);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendHtmlMail(MailSenderInfo mailSenderInfo) {
        try {
            MimeMessage mimeMessage = new MimeMessage(Session.getDefaultInstance(mailSenderInfo.getProperties(), mailSenderInfo.isValidate() ? new MailAuthenticator(mailSenderInfo.getUserName(), mailSenderInfo.getPassword()) : null));
            mimeMessage.setFrom(new InternetAddress(mailSenderInfo.getFromAddress()));
            mimeMessage.setRecipient(RecipientType.TO, new InternetAddress(mailSenderInfo.getToAddress()));
            mimeMessage.setSubject(mailSenderInfo.getSubject());
            mimeMessage.setSentDate(new Date());
            MimeMultipart mimeMultipart = new MimeMultipart();
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setContent(mailSenderInfo.getContent(), "text/html; charset=utf-8");
            mimeMultipart.addBodyPart(mimeBodyPart);
            mimeMessage.setContent(mimeMultipart);
            Transport.send(mimeMessage);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}