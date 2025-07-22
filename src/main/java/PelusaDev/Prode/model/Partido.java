package PelusaDev.Prode.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "partidos")
@AllArgsConstructor
@NoArgsConstructor
public class Partido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fecha_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Fecha fecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_local_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Equipo equipoLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_visitante_id", nullable = false)
    private Equipo equipoVisitante;

    @Column(nullable = true, name = "goles_local")
    private Integer golesLocal;

    @Column(nullable = true, name = "goles_visitante")
    private Integer golesVisitante;

    @Column(nullable = false, name = "finalizado", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean finalizado = false;


}