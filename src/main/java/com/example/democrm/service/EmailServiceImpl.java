package com.example.democrm.service;

import com.example.democrm.request.email.sendEmail;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailServiceImpl implements EmailService{
    @Value("${spring.mail.host}")
    private String smtpHost;

    @Value("${spring.mail.port}")
    private int smtpPort;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    public void sendEmail(String to, String subject, String content) throws MessagingException{
        Properties properties = new Properties();
        properties.put("mail.smtp.host",smtpHost);
        properties.put("mail.smtp.port",smtpPort);
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username,password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
//        message.setRecipient(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject(subject);
        message.setContent(content,"text/html");

        Transport.send(message);

    }

//    @Autowired
//    private JavaMailSender javaMailSender;

//    @Override
//    public void sendEmail(String to, String subject, String body) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(to);
//        message.setSubject(subject);
//        message.setText(body);
//
//        javaMailSender.send(message);
//    }

//    @Override
//    public void sendEmailWithButtons(sendEmail request) throws MessagingException {
////        String email = "nguyenthilien888899990000@gmail.com";
//        MimeMessage message = javaMailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//        try{
//            helper.setTo(request.getEmail());
//            helper.setSubject("Email lấy ý kiến khách hàng về nhân viên tư vấn!");
//
//            //Tao noi dung email
//            String emailContext = "<html><body>" +
//                    "<p>Xin chao quý khách, </p>" +
//                    "<p>Trong quá trình tư vấn của nhân viên với khách hàng chúng tôi muốn lấy ý kiến của khách hàng đối với nhân viên.</p>" +
//                    "<p>Quý khách vui lòng chọn vào 1 trong 5 sự lựa chọn ở dưới đây: </p>" +
//                    "<button><a href=\"http://localhost:8081/customer/4/status/1/update\">Hài lòng.</a></button>" +                //api đổi trạng thái là tiềm năng
//                    "<button><a href=\"http://your-api-url-1\">Thanh toán thành công.</a></button>" +   //api đổi trạng thái là thành công
//                    "<button><a href=\"http://your-api-url-1\">Chưa hài lòng.</a></button>" +           //api đổi trạng thái là chờ tư vấn
//                    "<button><a href=\"http://your-api-url-1\">Không quan tâm.</a></button>" +          //api đổi trạng thái là không quan tâm
//                    "</body></html>";
//            helper.setText(emailContext);
////            Transport.send(helper);
//        }catch (Exception ex){
//            throw new RuntimeException("Quá trình gửi email không thành công!");
//        }
//    }
}
