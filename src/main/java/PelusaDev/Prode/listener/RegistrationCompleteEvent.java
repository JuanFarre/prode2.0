package PelusaDev.Prode.listener;

import PelusaDev.Prode.model.Usuario;
import org.springframework.context.ApplicationEvent;

public class RegistrationCompleteEvent extends ApplicationEvent {

    // Usuario recién registrado.
    private Usuario user;
    // URL base de la aplicación, utilizada para generar enlaces de verificación.
    private String applicationUrl;

    // Constructor que inicializa el evento con el usuario y la URL de la aplicación.
    public RegistrationCompleteEvent(Usuario user, String applicationUrl) {
        super(user); // Llama al constructor de la clase base ApplicationEvent.
        this.user = user; // Asigna el usuario al campo correspondiente.
        this.applicationUrl = applicationUrl; // Asigna la URL de la aplicación.
    }

    // Método para obtener el usuario asociado al evento.
    public Usuario getUser() {
        return user; // Retorna el usuario asociado.
    }

    // Método para establecer el usuario asociado al evento.
    public void setUser(Usuario user) {
        this.user = user; // Asigna el nuevo usuario.
    }

    // Método para obtener la URL base de la aplicación.
    public String getApplicationUrl() {
        return applicationUrl; // Retorna la URL de la aplicación.
    }

    // Método para establecer la URL base de la aplicación.
    public void setApplicationUrl(String applicationUrl) {
        this.applicationUrl = applicationUrl; // Asigna la nueva URL de la aplicación.
    }
}
