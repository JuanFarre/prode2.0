package PelusaDev.Prode.service;

import PelusaDev.Prode.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Se intenta encontrar al usuario en la base de datos mediante el repositorio.
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        // Si se encuentra el usuario, se construye un objeto User de Spring Security con la información del usuario.
        String rol = user.getRol().name();
        System.out.println("Usuario encontrado: " + username + ", Rol: " + rol);
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + rol) // Asigna la autoridad basada en el rol
                .build();
    }
}