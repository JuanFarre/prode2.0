package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.EquipoDTO;
import PelusaDev.Prode.model.Equipo;

public class EquipoMapper {

    public static EquipoDTO toDTO(Equipo equipo) {
        EquipoDTO dto = new EquipoDTO();
        dto.setId(equipo.getId());
        dto.setNombre(equipo.getNombre());
        dto.setCiudad(equipo.getCiudad());
        dto.setEscudoUrl(equipo.getEscudoUrl());
        return dto;
    }

    public static Equipo toEntity(EquipoDTO dto) {
        Equipo equipo = new Equipo();
        equipo.setId(dto.getId());
        equipo.setNombre(dto.getNombre());
        equipo.setCiudad(dto.getCiudad());
        equipo.setEscudoUrl(dto.getEscudoUrl());
        return equipo;
    }
}
