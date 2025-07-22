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
        
        Partido partido = new Partido();
        partido.setId(dto.getId());
        partido.setFecha(fecha);
        partido.setEquipoLocal(equipoLocal);
        partido.setEquipoVisitante(equipoVisitante);
        partido.setGolesLocal(dto.getGolesLocal());
        partido.setGolesVisitante(dto.getGolesVisitante());
        partido.setFinalizado(dto.getFinalizado() != null ? dto.getFinalizado() : false);
        
        return partido;
    }

    public PartidoDTO toDTO(Partido partido) {
        PartidoDTO dto = new PartidoDTO();
        dto.setId(partido.getId());
        dto.setFechaId(partido.getFecha().getId());
        dto.setEquipoLocalId(partido.getEquipoLocal().getId());
        dto.setEquipoVisitanteId(partido.getEquipoVisitante().getId());
        dto.setGolesLocal(partido.getGolesLocal());
        dto.setGolesVisitante(partido.getGolesVisitante());
        dto.setFinalizado(partido.getFinalizado());
        return dto;
    }
}