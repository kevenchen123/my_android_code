package com.keven.utils.mail;

import java.util.Properties;

public class MailSenderInfo {
    private String[] attachFileNames;
    private String content;
    private String fromAddress;
    private String mailServerHost;
    private String mailServerPort = "25";
    private String password;
    private String subject;
    private String toAddress;
    private String userName;
    private boolean validate = true;

    public Properties getProperties() {
        Properties properties = new Properties();
        properties.put("mail.smtp.host", this.mailServerHost);
        properties.put("mail.smtp.port", this.mailServerPort);
        properties.put("mail.smtp.auth", this.validate ? "true" : "false");
        return properties;
    }

    public String getMailServerHost() {
        return this.mailServerHost;
    }

    public void setMailServerHost(String str) {
        this.mailServerHost = str;
    }

    public String getMailServerPort() {
        return this.mailServerPort;
    }

    public void setMailServerPort(String str) {
        this.mailServerPort = str;
    }

    public boolean isValidate() {
        return this.validate;
    }

    public void setValidate(boolean z) {
        this.validate = z;
    }

    public String[] getAttachFileNames() {
        return this.attachFileNames;
    }

    public void setAttachFileNames(String[] strArr) {
        this.attachFileNames = strArr;
    }

    public String getFromAddress() {
        return this.fromAddress;
    }

    public void setFromAddress(String str) {
        this.fromAddress = str;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String str) {
        this.password = str;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public void setToAddress(String str) {
        this.toAddress = str;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String str) {
        this.userName = str;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setSubject(String str) {
        this.subject = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }
}