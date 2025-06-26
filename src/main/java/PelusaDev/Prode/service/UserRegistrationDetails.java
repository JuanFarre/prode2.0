package PelusaDev.Prode.service;

import PelusaDev.Prode.model.Usuario;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
public class UserRegistrationDetails implements UserDetails {

    // Nombre de usuario, que será el correo electrónico del usuario.
    private String userName;
    // Contraseña del usuario.
    private String password;
    // Estado del usuario (si está habilitado o no para autenticarse).
    private boolean isEnabled;
    // Lista de autoridades o roles asociados al usuario.
    private List<GrantedAuthority> authorities;

    // Constructor que recibe un objeto Usuario y asigna sus valores a los campos de esta clase.
    public UserRegistrationDetails(Usuario user) {
        this.userName = user.getEmail(); // El email se utiliza como nombre de usuario.
        this.password = user.getPassword(); // La contraseña se toma del usuario.
        this.isEnabled = user.isEnabled(); // El estado habilitado se establece a partir del usuario.
    }

    // Método para obtener las autoridades o roles asociados al usuario.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities; // Devuelve la lista de roles.
    }

    // Método para obtener la contraseña del usuario.
    @Override
    public String getPassword() {
        return password; // Retorna la contraseña del usuario.
    }

    // Método para obtener el nombre de usuario.
    @Override
    public String getUsername() {
        return userName; // Retorna el nombre de usuario (correo electrónico).
    }

    // Indica si la cuenta del usuario no ha expirado. Aquí se asume que siempre es true.
    @Override
    public boolean isAccountNonExpired() {
        return true; // Siempre retorna true, indicando que la cuenta no está expirada.
    }

    // Indica si la cuenta del usuario no está bloqueada. Aquí se asume que siempre es true.
    @Override
    public boolean isAccountNonLocked() {
        return true; // Siempre retorna true, indicando que la cuenta no está bloqueada.
    }

    // Indica si las credenciales del usuario no han expirado. Aquí se asume que siempre es true.
    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Siempre retorna true, indicando que las credenciales no han expirado.
    }

    // Indica si el usuario está habilitado para autenticarse.
    @Override
    public boolean isEnabled() {
        return isEnabled; // Retorna el estado habilitado del usuario.
    }
}