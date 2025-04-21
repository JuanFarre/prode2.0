package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.RankingDTO;
import PelusaDev.Prode.model.Pronostico;

import java.util.List;
import java.util.Optional;

public interface IPronosticoService {

    List<Pronostico> findAll();

    Optional<Pronostico> findById(Long id);

    Pronostico save(Pronostico pronostico);

    void deleteById(Long id);

    List<Pronostico> findByPartidoFechaId(Long fechaId);

    public List<RankingDTO> getRankingPorFecha(Long fechaId);

    List<Pronostico> findByUsuarioAndFecha(Long usuarioId, Long fechaId);
}