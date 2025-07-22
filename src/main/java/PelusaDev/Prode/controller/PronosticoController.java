package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.PronosticoDTO;
import PelusaDev.Prode.dto.RankingDTO;
import PelusaDev.Prode.mapper.PronosticoMapper;
import PelusaDev.Prode.model.Pronostico;
import PelusaDev.Prode.service.PronosticoService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pronosticos")
public class PronosticoController {

    @Autowired
    private PronosticoService pronosticoService;

    @GetMapping
    public List<PronosticoDTO> getAllPronosticos() {
        return pronosticoService.findAll().stream()
                .map(PronosticoMapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PronosticoDTO> getPronosticoById(@PathVariable Long id) {
        return pronosticoService.findById(id)
                .map(pronostico -> ResponseEntity.ok(PronosticoMapper.toDTO(pronostico)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public PronosticoDTO createPronostico(@RequestBody PronosticoDTO pronosticoDTO) {
        Pronostico pronostico = PronosticoMapper.toEntity(pronosticoDTO);
        return PronosticoMapper.toDTO(pronosticoService.save(pronostico));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PronosticoDTO> updatePronostico(@PathVariable Long id, @RequestBody PronosticoDTO pronosticoDTO) {
        return pronosticoService.findById(id)
                .map(existingPronostico -> {
                    Pronostico updatedPronostico = PronosticoMapper.toEntity(pronosticoDTO);
                    updatedPronostico.setId(existingPronostico.getId());
                    return ResponseEntity.ok(PronosticoMapper.toDTO(pronosticoService.save(updatedPronostico)));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deletePronostico(@PathVariable Long id) {
        return pronosticoService.findById(id)
                .map(pronostico -> {
                    pronosticoService.deleteById(id);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.status(404).body("No se encuentra el pronóstico con ID: " + id));
    }

    @GetMapping("/usuario/{usuarioId}/fecha/{fechaId}")
    public ResponseEntity<List<PronosticoDTO>> getPronosticosByUsuarioAndFecha(
            @PathVariable Long usuarioId, @PathVariable Long fechaId) {
        List<PronosticoDTO> pronosticos = pronosticoService.findByUsuarioAndFecha(usuarioId, fechaId)
                .stream()
                .map(PronosticoMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(pronosticos);
    }
    
    @GetMapping("/ranking/fecha/{fechaId}")
    public ResponseEntity<List<RankingDTO>> getRankingPorFecha(@PathVariable Long fechaId) {
        List<RankingDTO> ranking = pronosticoService.getRankingPorFecha(fechaId);
        return ResponseEntity.ok(ranking);
    }
}