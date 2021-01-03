package com.learn.OnlineStore.service;


import com.learn.OnlineStore.model.User;
import com.learn.OnlineStore.model.VerificationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class EmailService {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    public static String getSiteUrl(HttpServletRequest request){
        String siteUrl=request.getRequestURL().toString();
        return siteUrl.replace(request.getServletPath(),"");

    }

    public void sendHtmlEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException {
        System.out.println("Sending");
        VerificationToken verificationToken=verificationTokenService.findByUser(user);

        //check if user have a token
        if(verificationToken!=null){
            String token=verificationToken.getToken();
            Context context=new Context();
            context.setVariable("title","Verify your email address");


            context.setVariable("link",siteUrl+"/activation?token="+token);

            //HTML template

            String body=templateEngine.process("verification",context);

            //send mail
            MimeMessage mimeMessage=javaMailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setTo(user.getEmail());
            helper.setFrom("semsobeso8@gmail.com","OnlineShop");
            helper.setSubject("Email address verification");
            helper.setText(body, true);
            javaMailSender.send(mimeMessage);



        }
        System.out.println("Sent");
    }

}
