package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.UsuarioDTO;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.service.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/usuarios")
@RestController
public class UsuarioController {

    @Autowired
    private IUsuarioService usuarioService;

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> getUsuarioById(@PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        return ResponseEntity.ok(usuarioService.getAllUsuarios());
    }

    @PostMapping
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO newUsuario = usuarioService.saveUsuario(usuarioDTO);
        return ResponseEntity.ok(newUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioDTO> updateUsuario(@RequestBody UsuarioDTO usuarioDTO, @PathVariable Long id) {
        return usuarioService.getUsuarioById(id)
                .map(existingUser -> ResponseEntity.ok(usuarioService.updateUsuario(usuarioDTO, id)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
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
}
