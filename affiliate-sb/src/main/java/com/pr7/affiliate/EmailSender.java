package com.pr7.affiliate;

import com.pr7.util.ReflectionUtil;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 *
 * @author Admin
 */
@Service
public class EmailSender {
    
    @Value("${SmtpHost}")
    protected String smtpHost;
    @Value("${SmtpUser}")
    protected String smtpUser;
    @Value("${SmtpSender}")
    protected String smtpSender;
    @Value("${SmtpBcc}")
    protected String smtpBcc;
    @Value("${SmtpPassword}")
    protected String smtpPassword;
    
    private static final Logger logger = LogManager.getLogger(EmailSender.class);
    
    public void send(String to, String subject, String body) throws MessagingException {
        send(new String[] { to }, subject, body);
    }

    public void send(String[] to, String subject, String body) throws MessagingException {
        if (StringUtils.isBlank(smtpSender)) {
            smtpSender = smtpUser;
        }
        
        Properties props = System.getProperties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.user", smtpUser);
        props.put("mail.smtp.password", smtpPassword);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(smtpSender));

        InternetAddress[] toAddress = new InternetAddress[to.length];

        for (int i = 0; i < to.length; i++) { 
            toAddress[i] = new InternetAddress(to[i]);
        }
        System.out.println(Message.RecipientType.TO);

        for (int i = 0; i < toAddress.length; i++) {
            message.addRecipient(Message.RecipientType.TO, toAddress[i]);
        }
        message.addRecipients(Message.RecipientType.BCC, smtpBcc);
        
        message.setSubject(subject, "utf-8");
        message.setContent(body, "text/html;charset=utf-8");
        message.setHeader("MIME-Version" , "1.0" );
        logger.debug (String.format(">>> Email Content:\n To:%s\nSubject:%s\nBody:%s",to,subject,body));
        Transport transport = session.getTransport("smtp");
        transport.connect(smtpHost, smtpUser, smtpPassword);
        transport.sendMessage(message, message.getAllRecipients());
        logger.debug(">>> Email Sent Successfully");
        transport.close();
    }
}
