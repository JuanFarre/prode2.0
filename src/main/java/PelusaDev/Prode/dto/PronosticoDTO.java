package PelusaDev.Prode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PronosticoDTO {

    private Long id;
    private Long usuarioId;
    private Long partidoId;
    private String resultadoPronosticado;
    private int puntosObtenidos; // Nuevo campo

}