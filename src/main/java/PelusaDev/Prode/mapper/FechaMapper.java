package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.FechaDTO;
import PelusaDev.Prode.model.Fecha;
import PelusaDev.Prode.model.Torneo;

public class FechaMapper {

    public static FechaDTO toDTO(Fecha fecha) {
        return new FechaDTO(
                fecha.getId(),
                fecha.getNombre(),
                fecha.getTorneo().getId(),
                fecha.getEmpezada()
        );
    }

    public static Fecha toEntity(FechaDTO dto, Torneo torneo) {
        Fecha fecha = new Fecha();
        fecha.setId(dto.getId());
        fecha.setNombre(dto.getNombre());
        fecha.setEmpezada(dto.getEmpezada() != null ? dto.getEmpezada() : false);
        fecha.setTorneo(torneo);
        return fecha;
    }
}
