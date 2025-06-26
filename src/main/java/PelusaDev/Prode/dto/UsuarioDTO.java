package PelusaDev.Prode.dto;

import PelusaDev.Prode.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {

    private String nombre;
    private String apellido;
    private String username;
    private String password;
    private String email;
    private int puntosTotales;
    private Rol rol;
    private boolean enabled;
    private LocalDateTime fechaRegistro;  // opcional, si lo necesitás en frontend o admin
}
