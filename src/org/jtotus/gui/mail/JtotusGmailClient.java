
/*
 *
 *
 * http://www.jscape.com/articles/pop_java_ssl_gmail.html
 * http://commons.apache.org/email/
 * http://pipoltek.blogspot.com/2008/02/sending-mail-using-gmail-smtp-server.html
 */
package org.jtotus.gui.mail;

import java.security.Security;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Callable;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

// Common mail example:
//    public boolean pushMail(String login,
//                            String password,
//                            String subject,