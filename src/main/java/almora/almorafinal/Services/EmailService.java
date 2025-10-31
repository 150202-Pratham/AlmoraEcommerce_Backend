package almora.almorafinal.Services;

import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender ;

    public void sendOrderConfirmation(String toEmail , String orderId , double totalAmount  ){
        SimpleMailMessage message = new SimpleMailMessage() ;

        message.setTo(toEmail);
        message.setSubject("Order Confirmation- Almora Fashion Store");
        message.setText("Dear Customer,\n\nYour order with ID:" + orderId +
                   " has been successfully placed.\nTotal Amount: ₹" + totalAmount +
                "\n\nThank you for shopping with us!\n\nTeam Almora Fashion");


        mailSender.send(message) ;

    }





}
