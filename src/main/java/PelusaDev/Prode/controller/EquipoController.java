package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.EquipoDTO;
import PelusaDev.Prode.service.IEquipoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/equipos")
@CrossOrigin
public class EquipoController {

    @Autowired
    private IEquipoService equipoService;

    @GetMapping
    public ResponseEntity<List<EquipoDTO>> getAllEquipos() {
        return ResponseEntity.ok(equipoService.getAllEquipos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoDTO> getEquipoById(@PathVariable Long id) {
        return equipoService.getEquipoById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/crear")
    public ResponseEntity<EquipoDTO> createEquipo(@RequestBody EquipoDTO equipoDTO) {
        return ResponseEntity.ok(equipoService.saveEquipo(equipoDTO));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/editar/{id}")
    public ResponseEntity<EquipoDTO> updateEquipo(@RequestBody EquipoDTO equipoDTO, @PathVariable Long id) {
        return equipoService.getEquipoById(id)
                .map(existingEquipo -> ResponseEntity.ok(equipoService.updateEquipo(equipoDTO, id)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteEquipo(@PathVariable Long id) {
        return equipoService.getEquipoById(id)
                .map(equipo -> {
                    equipoService.deleteEquipo(id);
                    return ResponseEntity.<Void>noContent().build();
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
