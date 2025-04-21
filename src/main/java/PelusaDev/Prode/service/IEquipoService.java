package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.EquipoDTO;
import java.util.List;
import java.util.Optional;

public interface IEquipoService {
    List<EquipoDTO> getAllEquipos();
    Optional<EquipoDTO> getEquipoById(Long id);
    EquipoDTO saveEquipo(EquipoDTO equipoDTO);
    EquipoDTO updateEquipo(EquipoDTO equipoDTO, Long id);
    void deleteEquipo(Long id);
}
