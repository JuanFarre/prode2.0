package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.PartidoDTO;
import PelusaDev.Prode.mapper.PartidoMapper;
import PelusaDev.Prode.model.Partido;
import PelusaDev.Prode.service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    @Autowired
    private PartidoService partidoService;

    @Autowired
    private PartidoMapper partidoMapper;

    @GetMapping
    public List<PartidoDTO> getAllPartidos() {
        return partidoService.findAll().stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidoDTO> getPartidoById(@PathVariable Long id) {
        return partidoService.findById(id)
                .map(partido -> ResponseEntity.ok(partidoMapper.toDTO(partido)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public PartidoDTO createPartido(@RequestBody PartidoDTO partidoDTO) {
        Partido partido = partidoMapper.toEntity(partidoDTO);
        return partidoMapper.toDTO(partidoService.save(partido));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PartidoDTO> updatePartido(@PathVariable Long id, @RequestBody PartidoDTO partidoDTO) {
        return partidoService.findById(id)
                .map(existingPartido -> {
                    Partido updatedPartido = partidoMapper.toEntity(partidoDTO);
                    updatedPartido.setId(existingPartido.getId());
                    
                    // Si se están actualizando los goles, marcar como finalizado automáticamente
                    if (partidoDTO.getGolesLocal() != null && partidoDTO.getGolesVisitante() != null) {
                        updatedPartido.setFinalizado(true);
                    }
                    
                    return ResponseEntity.ok(partidoMapper.toDTO(partidoService.save(updatedPartido)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/finalizar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PartidoDTO> finalizarPartido(@PathVariable Long id) {
        return partidoService.findById(id)
                .map(partido -> {
                    partido.setFinalizado(true);
                    return ResponseEntity.ok(partidoMapper.toDTO(partidoService.save(partido)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePartido(@PathVariable Long id) {
        return partidoService.findById(id)
                .map(partido -> {
                    partidoService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/fecha/{fechaId}")
    public ResponseEntity<List<PartidoDTO>> getPartidosByFechaId(@PathVariable Long fechaId) {
        List<PartidoDTO> partidos = partidoService.findByFechaId(fechaId).stream()
                .map(partidoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(partidos);
    }
}