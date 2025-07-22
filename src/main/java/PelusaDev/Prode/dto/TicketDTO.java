package PelusaDev.Prode.dto;

import PelusaDev.Prode.model.Fecha;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.model.Pronostico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Long usuarioId;
    private LocalDateTime fechaCreacion;
    private Long fechaId;
    private List<PronosticoDTO> pronosticos;
    private Integer puntosTotales;
}
