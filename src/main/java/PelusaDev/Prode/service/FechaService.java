package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.FechaDTO;
import PelusaDev.Prode.mapper.FechaMapper;
import PelusaDev.Prode.model.Fecha;
import PelusaDev.Prode.model.Torneo;
import PelusaDev.Prode.repository.FechaRepository;
import PelusaDev.Prode.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FechaService implements IFechaService {

    @Autowired
    private FechaRepository fechaRepository;

    @Autowired
    private TorneoRepository torneoRepository;

    @Override
    public List<FechaDTO> getAllFechas() {
        return fechaRepository.findAll().stream()
                .map(FechaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<FechaDTO> getFechaById(Long id) {
        return fechaRepository.findById(id).map(FechaMapper::toDTO);
    }

    @Override
    public FechaDTO saveFecha(FechaDTO fechaDTO) {
        Torneo torneo = torneoRepository.findById(fechaDTO.getTorneoId())
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        Fecha fecha = FechaMapper.toEntity(fechaDTO, torneo);
        Fecha saved = fechaRepository.save(fecha);
        return FechaMapper.toDTO(saved);
    }

    @Override
    public FechaDTO updateFecha(Long id, FechaDTO fechaDTO) {
        Torneo torneo = torneoRepository.findById(fechaDTO.getTorneoId())
                .orElseThrow(() -> new RuntimeException("Torneo no encontrado"));

        Fecha fecha = FechaMapper.toEntity(fechaDTO, torneo);
        fecha.setId(id);
        Fecha updated = fechaRepository.save(fecha);
        return FechaMapper.toDTO(updated);
    }

    @Override
    public void deleteFecha(Long id) {
        fechaRepository.deleteById(id);
    }
}
