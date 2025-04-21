package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.UsuarioDTO;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    Optional<UsuarioDTO> getUsuarioById(Long id);

    List<UsuarioDTO> getAllUsuarios();

    UsuarioDTO saveUsuario(UsuarioDTO usuarioDTO);

    void deleteUsuario(Long id);

    List<UsuarioDTO> getTablaGeneral();

    List<UsuarioDTO> getTablaPorFecha(Long fechaId);

    UsuarioDTO updateUsuario(UsuarioDTO usuarioDTO, Long id);
}
