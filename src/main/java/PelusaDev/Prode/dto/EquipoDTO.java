package PelusaDev.Prode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EquipoDTO {
    private Long id;
    private String nombre;
    private String ciudad;
    private String escudoUrl;
}
