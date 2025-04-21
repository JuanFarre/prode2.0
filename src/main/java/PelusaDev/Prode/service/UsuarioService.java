package PelusaDev.Prode.service;

import PelusaDev.Prode.mapper.UsuarioMapper;
import PelusaDev.Prode.dto.UsuarioDTO;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
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
                    Usuario usuario = UsuarioMapper.toEntity(usuarioDTO);
                    usuario.setId(id); // Mantener el mismo ID
                    Usuario updatedUsuario = usuarioRepository.save(usuario);
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
