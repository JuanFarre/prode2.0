package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.TorneoDTO;
import PelusaDev.Prode.mapper.TorneoMapper;
import PelusaDev.Prode.model.Torneo;
import PelusaDev.Prode.service.TorneoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    @Autowired
    private TorneoService torneoService;

    @GetMapping
    public List<TorneoDTO> getAllTorneos() {
        return torneoService.findAll().stream()
                .map(TorneoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TorneoDTO> getTorneoById(@PathVariable Long id) {
        return torneoService.findById(id)
                .map(torneo -> ResponseEntity.ok(TorneoMapper.toDTO(torneo)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public TorneoDTO createTorneo(@RequestBody TorneoDTO torneoDTO) {
        Torneo torneo = TorneoMapper.toEntity(torneoDTO);
        return TorneoMapper.toDTO(torneoService.save(torneo));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TorneoDTO> updateTorneo(@PathVariable Long id, @RequestBody TorneoDTO torneoDTO) {
        return torneoService.findById(id)
                .map(existingTorneo -> {
                    Torneo updatedTorneo = TorneoMapper.toEntity(torneoDTO);
                    updatedTorneo.setId(existingTorneo.getId());
                    return ResponseEntity.ok(TorneoMapper.toDTO(torneoService.save(updatedTorneo)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteTorneo(@PathVariable Long id) {
        return torneoService.findById(id)
                .map(torneo -> {
                    torneoService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}