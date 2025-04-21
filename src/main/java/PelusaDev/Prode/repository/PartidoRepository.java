package PelusaDev.Prode.repository;

import PelusaDev.Prode.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartidoRepository extends JpaRepository<Partido, Long> {
    List<Partido> findByFechaId(Long fechaId);
}
