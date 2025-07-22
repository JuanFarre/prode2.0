package PelusaDev.Prode.listener;

import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.service.AuthServiceImpl;
import PelusaDev.Prode.repository.UsuarioRepository;
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
    
    // Activa esta bandera para simular el envío de correos sin intentar conectarse a Gmail
    private static final boolean SIMULAR_ENVIO_CORREO = false;

    @Autowired
    private AuthServiceImpl userService;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

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
            if (SIMULAR_ENVIO_CORREO) {
                simulateEmailSending(url);
            } else {
                sendVerificationEmail(url);
            }
            log.info("Correo de verificación enviado a: {}", theUser.getEmail());
        } catch (UnsupportedEncodingException | MessagingException e) {
            log.error("Error al enviar el correo de verificación: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Método para simular el envío de correo sin realmente conectarse a Gmail
     * Útil para pruebas de desarrollo
     */
    private void simulateEmailSending(String url) {
        log.info("SIMULANDO ENVÍO DE CORREO - No se está intentando enviar realmente");
        log.info("URL de verificación para {}: {}", theUser.getEmail(), url);
        log.info("En producción, el usuario recibiría un correo con un enlace para hacer clic");
        
        // Auto-habilitar al usuario para pruebas de desarrollo
        theUser.setEnabled(true);
        usuarioRepository.save(theUser);
        log.info("Usuario auto-habilitado para pruebas: {}", theUser.getEmail());
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

        messageHelper.setFrom("juanfarre99@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);

        log.info("Enviando correo...");
        try {
            mailSender.send(message);
            log.info("Correo enviado exitosamente a: {}", theUser.getEmail());
        } catch (Exception e) {
            log.error("Error al enviar el correo: {} - Mensaje: {}", e.getClass().getName(), e.getMessage());
            if (e.getCause() != null) {
                log.error("Causa: {} - {}", e.getCause().getClass().getName(), e.getCause().getMessage());
            }
            throw new RuntimeException("Error al enviar el correo de verificación", e);
        }
    }
}