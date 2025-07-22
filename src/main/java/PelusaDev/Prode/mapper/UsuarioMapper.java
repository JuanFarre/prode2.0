package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.UsuarioDTO;
import PelusaDev.Prode.model.Rol;
import PelusaDev.Prode.model.Usuario;

public class UsuarioMapper {

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNombre(dto.getNombre());
        usuario.setApellido(dto.getApellido());
        usuario.setUsername(dto.getUsername());
        
        // Only set password if it's not null and not empty
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            usuario.setPassword(dto.getPassword());
        }
        
        usuario.setEmail(dto.getEmail());
        usuario.setPuntosTotales(dto.getPuntosTotales());

        // Manejo de Rol, evitando valores nulos
        if (dto.getRol() != null) {
            usuario.setRol(dto.getRol());
        } else {
            usuario.setRol(Rol.USER);  // Valor por defecto
        }

        return usuario;
    }

    public static UsuarioDTO toDTO(Usuario usuario) {
        return new UsuarioDTO(
                usuario.getNombre(),
                usuario.getApellido(),
                usuario.getUsername(),
                null, // Never send password back to client
                usuario.getEmail(),
                usuario.getPuntosTotales(),
                usuario.getRol(),  // No hay conversión necesaria porque ya es un enum
                usuario.isEnabled(),
                usuario.getFechaRegistro()  // Si necesitas la fecha de registro
        );
    }
}
