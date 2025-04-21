package PelusaDev.Prode.repository;

import PelusaDev.Prode.model.Pronostico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PronosticoRepository extends JpaRepository<Pronostico, Long> {

    @Query("SELECT p FROM Pronostico p WHERE p.partido.fecha.id = :fechaId")
    List<Pronostico> findByPartidoFechaId(@Param("fechaId") Long fechaId);

    @Query("SELECT p FROM Pronostico p WHERE p.partido.fecha.id = :fechaId AND p.usuario.id = :usuarioId")
    List<Pronostico> findByUsuarioAndFecha(@Param("usuarioId") Long usuarioId, @Param("fechaId") Long fechaId);

    @Query("SELECT p FROM Pronostico p WHERE p.partido.id = :partidoId")
    List<Pronostico> findByPartidoId(@Param("partidoId") Long partidoId);
}
