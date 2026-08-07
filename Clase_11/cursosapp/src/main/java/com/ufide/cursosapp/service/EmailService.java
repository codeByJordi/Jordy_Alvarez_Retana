package com.ufide.cursosapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
 import org.springframework.stereotype.Service;

@Service
public class EmailService {
    

    @Autowired
    private JavaMailSender eMailSender;

    public void enviarMensaje(String mail, String enlace) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mail);
        mailMessage.setSubject("Restablecer contrasena");
        mailMessage.setText("Aqui el enlace para restablecer contrasena: " + enlace);

        eMailSender.send(mailMessage);

    }

}
