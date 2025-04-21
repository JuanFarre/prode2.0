package PelusaDev.Prode.repository;

import PelusaDev.Prode.model.Fecha;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FechaRepository extends JpaRepository<Fecha, Long> {
}
