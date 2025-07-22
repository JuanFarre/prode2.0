package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.UsuarioDTO;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/usuarios")
@RestController
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        System.out.println("[UsuarioController] Solicitando usuario con ID: " + id);
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO newUsuario = usuarioService.saveUsuario(usuarioDTO);
        return ResponseEntity.ok(newUsuario);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN') or @securityUtils.isCurrentUser(#id)")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody UsuarioDTO usuarioDTO, @PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(existingUser -> ResponseEntity.ok(usuarioService.updateUsuario(usuarioDTO, id)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteUsuario(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(usuario -> {
                    usuarioService.deleteUsuario(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/tabla-general")
    public ResponseEntity<List<UsuarioDTO>> getTablaGeneral() {
        return ResponseEntity.ok(usuarioService.getTablaGeneral());
    }

    @GetMapping("/tabla-por-fecha/{fechaId}")
    public ResponseEntity<List<UsuarioDTO>> getTablaPorFecha(@PathVariable Long fechaId) {
        return ResponseEntity.ok(usuarioService.getTablaPorFecha(fechaId));
    }

    @GetMapping("/debug-auth")
    public ResponseEntity<String> debugAuth() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return ResponseEntity.ok("No hay autenticación establecida");
        }

        StringBuilder info = new StringBuilder();
        info.append("Usuario: ").append(auth.getName()).append("\n");
        info.append("Autenticado: ").append(auth.isAuthenticated()).append("\n");
        info.append("Autoridades: ").append(auth.getAuthorities()).append("\n");

        if (auth.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) auth.getPrincipal();
            info.append("UserDetails.username: ").append(userDetails.getUsername()).append("\n");
            info.append("UserDetails.authorities: ").append(userDetails.getAuthorities()).append("\n");
        }

        return ResponseEntity.ok(info.toString());
    }
}
