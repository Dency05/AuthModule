package com.example.sm.common.utils;

import com.example.sm.common.enums.CustomHTTPHeaders;
import com.example.sm.common.exception.NotFoundException;
import com.example.sm.common.model.EmailModel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.util.Properties;
import java.util.Random;
@Component
@Slf4j
public class Utils {

    @Autowired
    MessageSource messageSource;

    private static final String TOKEN_NOT_FOUND = "Token not found";
    public static String generateVerificationToken(int length) {
        // Using random method
        Random random_method = new Random();
        StringBuilder otp = new StringBuilder();
        for(int i =0; i< length; i++){
            otp.append(random_method.nextInt(10));
        }
        return otp.toString();
    }
    public static String getTokenFromHeaders(HttpServletRequest request) throws NotFoundException {
        String jwtToken = request.getHeader(CustomHTTPHeaders.TOKEN.toString());
        if(jwtToken ==null){
            throw new NotFoundException(TOKEN_NOT_FOUND);
        }
        return jwtToken;
    }
    public static String encodeBase64(String password) {
        byte[] pass = Base64.encodeBase64(password.getBytes());
        String actualString = new String(pass);
        System.out.println(actualString);
        return actualString;
    }
    public static String decodeBase64(String password) {
        byte[] actualByte = Base64.decodeBase64(password);
        String actualString = new String(actualByte);
        System.out.println(actualString);
        return actualString;
    }
    /*
    public static List<com.example.auth.enumUser.Role> getAllRoles(Class<com.example.auth.enumUser.Role> authorizationClass) {
        List<com.example.auth.common.model.Role> roleList = new ArrayList<>();
        com.example.auth.enumUser.Role[] authList = com.example.auth.enumUser.Role.values();
        for (com.example.auth.enumUser.Role authorization : authList) {
            com.example.auth.common.model.Role role = new com.example.auth.common.model.Role();
            role.setSoftDelete(false);
            role.setRoleDescription(authorization.name());
            role.setSpecificRole(authorization.name());
            roleList.add(role);
        }
        return roleList;
    }

     */

    public void sendEmailNow( EmailModel emailModel) {
        try {
            //SMTP Simple mail transfer protocol
            /*if(configuration==null){
                configuration = configService.getConfiguration();
            }*/

            if(StringUtils.isEmpty(emailModel.getSubject())){
                emailModel.setSubject(emailModel.getSubject());
            }

            // Recipient's email ID needs to be mentioned.
            // Sender's email ID needs to be mentioned
            String from ="developer@techroversolutions.com" ;

            String username = "developer@techroversolutions.com";// change accordingly //
            String password = "Ancubate@2019";// change accordingly //
            Properties props = new Properties();
            props.put("mail.smtp.auth", "true");//true
            props.put("mail.smtp.starttls.enable", "true");//true
            //props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.host", "smtp.office365.com");//smtp.office365.com
            props.put("mail.smtp.port", "587");//587
            /*props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.protocols","TLSv1.2");
            props.put("mail.smtp.socketFactory.fallback", "false");*/
            // Get the Session object.
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            try {
                // Create a default MimeMessage object.
                MimeMessage message = new MimeMessage(session);
                MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
                // Set From: header field of the header.
                helper.setFrom(new InternetAddress(from));
                // Set To: header field of the header.
                helper.setTo(emailModel.getTo());
                /*
                if(emailModel.getCc() != null && emailModel.getCc().size() != 0){
                    String[] cc = new String[emailModel.getCc().size()];
                    emailModel.getCc().toArray(cc);
                    helper.setCc(cc);
                }
                if(emailModel.getBcc() != null && emailModel.getBcc().size() != 0){
                    String[] bcc = new String[emailModel.getBcc().size()];
                    emailModel.getBcc().toArray(bcc);
                    helper.setBcc(bcc);
                }

                */
                // Set Subject: header field
                helper.setSubject(emailModel.getSubject());
                // Now set the actual message
                helper.setText(emailModel.getMessage(),true);
              /*  if(emailModel.getAttachmentList() != null && emailModel.getAttachmentList().size() != 0){
                    emailModel.getAttachmentList().forEach(attachment->{
                        try {
                            helper.addAttachment(attachment.getFileName(), new URLDataSource(new URL(attachment.getAttachmentUrl())));
                        } catch (MessagingException | IOException e) {
                            log.info("Unable to attach File");
                            e.printStackTrace();
                        }
                    });
                }
*/
                // Send message
                new Thread(() ->{
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }).start();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        } catch (Exception ignored) {}
    }


}
