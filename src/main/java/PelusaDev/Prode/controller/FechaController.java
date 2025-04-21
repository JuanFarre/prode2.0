package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.FechaDTO;
import PelusaDev.Prode.service.IFechaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fechas")
public class FechaController {

    @Autowired
    private IFechaService fechaService;

    @GetMapping
    public List<FechaDTO> getAllFechas() {
        return fechaService.getAllFechas();
    }

    @GetMapping("/{id}")
    public ResponseEntity<FechaDTO> getFechaById(@PathVariable Long id) {
        return fechaService.getFechaById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<FechaDTO> createFecha(@RequestBody FechaDTO fechaDTO) {
        return ResponseEntity.ok(fechaService.saveFecha(fechaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FechaDTO> updateFecha(@PathVariable Long id, @RequestBody FechaDTO fechaDTO) {
        return ResponseEntity.ok(fechaService.updateFecha(id, fechaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFecha(@PathVariable Long id) {
        fechaService.deleteFecha(id);
        return ResponseEntity.noContent().build();
    }
}
