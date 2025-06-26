package PelusaDev.Prode.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Rol rol = Rol.USER;

    private int puntosTotales;

    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    @ElementCollection
    @CollectionTable(name = "puntos_por_fecha", joinColumns = @JoinColumn(name = "usuario_id"))
    @MapKeyColumn(name = "fecha_id")
    @Column(name = "puntos")
    private Map<Long, Integer> puntosPorFecha = new HashMap<>();

    // Este método se ejecuta antes de guardar el usuario
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
    }
}
