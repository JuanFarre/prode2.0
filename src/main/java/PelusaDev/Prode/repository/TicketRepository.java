package PelusaDev.Prode.repository;

import PelusaDev.Prode.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUsuarioId(Long usuarioId);
    
    @Query("SELECT DISTINCT t FROM Ticket t JOIN t.pronosticos p WHERE p.partido.id = :partidoId")
    List<Ticket> findByPronosticosPartidoId(@Param("partidoId") Long partidoId);
    
    boolean existsByUsuarioIdAndFechaId(Long usuarioId, Long fechaId);
}
