/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import static java.lang.ProcessBuilder.Redirect.to;
import static java.util.Date.from;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 *
 * @author dovan
 */
@Service
public class SendMail {

    @Autowired
    JavaMailSender sender;

    public void send(String toMail, String subject, String body) {
        try {
            //        SimpleMailMessage smm = new SimpleMailMessage();
//        smm.setFrom("pthtttql2023@gmail.com");
//        smm.setTo(toMail);
//        smm.setSubject(subject);
//        smm.setText(message);

//        sender.send(smm);
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("pthtttql2023@gmail.com");
            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(body, true);
            sender.send(message);
        } catch (Exception e) {
            System.out.println(e);
        }

    }
}
