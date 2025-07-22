package PelusaDev.Prode.utils;

import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SecurityUtils {

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Checks if the current authenticated user is the user with the given ID
     * 
     * @param userId The ID of the user to check
     * @return true if the current user is the user with the given ID, false otherwise
     */
    public boolean isCurrentUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            System.out.println("[SecurityUtils] No hay autenticación o no está autenticado");
            return false;
        }
        
        String username = authentication.getName();
        System.out.println("[SecurityUtils] Usuario autenticado: " + username);
        System.out.println("[SecurityUtils] Autoridades: " + authentication.getAuthorities());
        
        Optional<Usuario> user = usuarioRepository.findByUsername(username);
        
        if (user.isEmpty()) {
            System.out.println("[SecurityUtils] Usuario no encontrado en la base de datos: " + username);
            return false;
        }
        
        System.out.println("[SecurityUtils] ID del usuario: " + user.get().getId() + ", ID solicitado: " + userId);
        return user.get().getId().equals(userId);
    }
}
