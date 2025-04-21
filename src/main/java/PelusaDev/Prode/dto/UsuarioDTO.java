package PelusaDev.Prode.dto;

import PelusaDev.Prode.model.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UsuarioDTO {

    private String nombre;
    private String apellido;
    private String password;
    private String username;
    private String email;
    private int puntosTotales;
    private Rol rol;
}
