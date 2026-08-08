package com.ufide.biblioapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarMensaje(String correo, String enlace) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(correo);
        message.setSubject("Restablecer Contrasena");
        message.setText("Sitio de recuperar contrasena: " + enlace);

        mailSender.send(message);


    }
}
