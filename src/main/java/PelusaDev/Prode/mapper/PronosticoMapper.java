package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.PronosticoDTO;
import PelusaDev.Prode.model.Pronostico;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.model.Partido;
import PelusaDev.Prode.repository.UsuarioRepository;
import PelusaDev.Prode.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PronosticoMapper {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    public Pronostico toEntity(PronosticoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId()).orElse(null);
        Partido partido = partidoRepository.findById(dto.getPartidoId()).orElse(null);
        return new Pronostico(dto.getId(), usuario, partido, dto.getResultadoPronosticado(), dto.getPuntosObtenidos());
    }

    public PronosticoDTO toDTO(Pronostico pronostico) {
        return new PronosticoDTO(
                pronostico.getId(),
                pronostico.getUsuario().getId(),
                pronostico.getPartido().getId(),
                pronostico.getResultadoPronosticado(),
                pronostico.getPuntosObtenidos() // Nuevo campo
        );
    }
}