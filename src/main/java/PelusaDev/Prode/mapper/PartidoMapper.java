package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.PartidoDTO;
import PelusaDev.Prode.model.Partido;
import PelusaDev.Prode.model.Fecha;
import PelusaDev.Prode.model.Equipo;
import PelusaDev.Prode.repository.FechaRepository;
import PelusaDev.Prode.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PartidoMapper {

    @Autowired
    private FechaRepository fechaRepository;

    @Autowired
    private EquipoRepository equipoRepository;

    public Partido toEntity(PartidoDTO dto) {
        Fecha fecha = fechaRepository.findById(dto.getFechaId()).orElse(null);
        Equipo equipoLocal = equipoRepository.findById(dto.getEquipoLocalId()).orElse(null);
        Equipo equipoVisitante = equipoRepository.findById(dto.getEquipoVisitanteId()).orElse(null);
        return new Partido(dto.getId(), fecha, equipoLocal, equipoVisitante, dto.getGolesLocal(), dto.getGolesVisitante());
    }

    public PartidoDTO toDTO(Partido partido) {
        return new PartidoDTO(partido.getId(), partido.getFecha().getId(), partido.getEquipoLocal().getId(), partido.getEquipoVisitante().getId(), partido.getGolesLocal(), partido.getGolesVisitante());
    }
}