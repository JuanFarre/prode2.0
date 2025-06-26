package PelusaDev.Prode.listener;

import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.service.AuthServiceImpl;
import jakarta.mail.AuthenticationFailedException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private static final Logger log = LoggerFactory.getLogger(RegistrationCompleteEventListener.class);

    @Autowired
    private AuthServiceImpl userService;

    @Autowired
    private JavaMailSender mailSender;
    private Usuario theUser;

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        log.info("Evento de registro recibido para el usuario: {}", event.getUser().getEmail());
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        log.info("Token de verificación generado y guardado: {}", verificationToken);

        String url = "http://localhost:8080/api/auth/verifyEmail?token=" + verificationToken;
        log.info("URL de verificación generada: {}", url);

        try {
            sendVerificationEmail(url);
            log.info("Correo de verificación enviado a: {}", theUser.getEmail());
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("Error al enviar el correo de verificación: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public void sendVerificationEmail(String url) throws UnsupportedEncodingException, MessagingException {
        log.info("Preparando el correo de verificación para: {}", theUser.getEmail());
        String subject = "Verificación de Correo Electrónico";
        String senderName = "Servicio de Registro de Usuarios";
        String mailContent = "<p>Hola, " + theUser.getNombre() + ",</p>"
                + "<p>Gracias por registrarte con nosotros.</p>"
                + "<p>Por favor, haz clic en el enlace a continuación para completar tu registro:</p>"
                + "<a href=\"" + url + "\">Verifica tu correo electrónico para activar tu cuenta</a>"
                + "<p>Gracias,<br>Servicio de Registro de Usuarios</p>";

        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message);

        messageHelper.setFrom("pruebatech370@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        log.info("Enviando correo...");
        try {
            mailSender.send(message);
            log.info("Correo enviado exitosamente.");
        } catch (Exception e) {
            log.error("Error inesperado al enviar el correo: {}", e.getMessage(), e);
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }
    }
}