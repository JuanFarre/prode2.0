package PelusaDev.Prode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PartidoDTO {

    private Long id;
    private Long fechaId;
    private Long equipoLocalId;
    private Long equipoVisitanteId;
    private Integer golesLocal;
    private Integer golesVisitante;
}