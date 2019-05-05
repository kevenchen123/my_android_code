package com.keven.utils.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

public class MailAuthenticator extends Authenticator {
    String password = null;
    String userName = null;

    public MailAuthenticator(String str, String str2) {
        this.userName = str;
        this.password = str2;
    }

    /* Access modifiers changed, original: protected */
    public PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(this.userName, this.password);
    }
}