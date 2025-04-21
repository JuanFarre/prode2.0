package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.FechaDTO;

import java.util.List;
import java.util.Optional;

public interface IFechaService {
    List<FechaDTO> getAllFechas();
    Optional<FechaDTO> getFechaById(Long id);
    FechaDTO saveFecha(FechaDTO fechaDTO);
    FechaDTO updateFecha(Long id, FechaDTO fechaDTO);
    void deleteFecha(Long id);
}
