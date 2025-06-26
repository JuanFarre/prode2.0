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

    public static Pronostico toEntity(PronosticoDTO dto) {
        // NOTA: No se puede usar @Autowired en métodos estáticos, así que debes setear usuario y partido en el servicio
        Pronostico pronostico = new Pronostico();
        pronostico.setId(dto.getId());
        pronostico.setResultadoPronosticado(dto.getResultadoPronosticado());
        pronostico.setPuntosObtenidos(dto.getPuntosObtenidos());
        return pronostico;
    }

    public static PronosticoDTO toDTO(Pronostico pronostico) {
        return new PronosticoDTO(
                pronostico.getId(),
                pronostico.getUsuario() != null ? pronostico.getUsuario().getId() : null,
                pronostico.getPartido() != null ? pronostico.getPartido().getId() : null,
                pronostico.getResultadoPronosticado(),
                pronostico.getPuntosObtenidos()
        );
    }
}