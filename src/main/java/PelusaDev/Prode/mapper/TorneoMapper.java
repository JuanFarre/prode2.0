package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.TorneoDTO;
import PelusaDev.Prode.model.Torneo;

public class TorneoMapper {

    public static Torneo toEntity(TorneoDTO dto) {
        return new Torneo(dto.getId(), dto.getNombre(), dto.getAnio());
    }

    public static TorneoDTO toDTO(Torneo torneo) {
        return new TorneoDTO(torneo.getId(), torneo.getNombre(), torneo.getAnio());
    }
}