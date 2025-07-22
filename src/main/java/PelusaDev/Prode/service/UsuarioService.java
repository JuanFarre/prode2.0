package PelusaDev.Prode.service;

import PelusaDev.Prode.mapper.UsuarioMapper;
import PelusaDev.Prode.dto.UsuarioDTO;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Optional<UsuarioDTO> getUsuarioById(Long id) {
        return usuarioRepository.findById(id).map(UsuarioMapper::toDTO);
    }

    @Override
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO) {
        // Verificar si el username ya existe
        if (usuarioRepository.existsByUsername(usuarioDTO.getUsername())) {
            throw new RuntimeException("El nombre de usuario ya está en uso");
        }
        
        // Verificar si el email ya existe
        if (usuarioRepository.existsByEmail(usuarioDTO.getEmail())) {
            throw new RuntimeException("El correo electrónico ya está registrado");
        }
        
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
        
        // Encode password if provided
        if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
        }
        
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return UsuarioMapper.toDTO(savedUsuario);
    }

    @Override
    public void deleteUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioDTO updateUsuario(UsuarioDTO usuarioDTO, Long id) {
        return usuarioRepository.findById(id)
                .map(existingUser -> {
                    // Update fields from DTO
                    existingUser.setNombre(usuarioDTO.getNombre());
                    existingUser.setApellido(usuarioDTO.getApellido());
                    existingUser.setEmail(usuarioDTO.getEmail());
                    existingUser.setPuntosTotales(usuarioDTO.getPuntosTotales());
                    
                    // Don't update password if it's null in the DTO
                    // Password should be encoded before saving if it's provided
                    if (usuarioDTO.getPassword() != null && !usuarioDTO.getPassword().isEmpty()) {
                        existingUser.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));
                    }
                    
                    // Update username only if it doesn't already exist for another user
                    if (!existingUser.getUsername().equals(usuarioDTO.getUsername())) {
                        if (usuarioRepository.findByUsername(usuarioDTO.getUsername()).isPresent()) {
                            throw new RuntimeException("Username already exists");
                        }
                        existingUser.setUsername(usuarioDTO.getUsername());
                    }
                    
                    // Update role if provided and has permission (should be checked at controller level)
                    if (usuarioDTO.getRol() != null) {
                        existingUser.setRol(usuarioDTO.getRol());
                    }
                    
                    Usuario updatedUsuario = usuarioRepository.save(existingUser);
                    return UsuarioMapper.toDTO(updatedUsuario);
                })
                .orElse(null); // Manejar caso de usuario no encontrado
    }



    public List<UsuarioDTO> getTablaGeneral() {
        return usuarioRepository.findAll().stream()
                .sorted((u1, u2) -> Integer.compare(u2.getPuntosTotales(), u1.getPuntosTotales()))
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<UsuarioDTO> getTablaPorFecha(Long fechaId) {
        return usuarioRepository.findAll().stream()
                .sorted((u1, u2) -> Integer.compare(
                        u2.getPuntosPorFecha().getOrDefault(fechaId, 0),
                        u1.getPuntosPorFecha().getOrDefault(fechaId, 0)))
                .map(UsuarioMapper::toDTO)
                .collect(Collectors.toList());
    }
}
