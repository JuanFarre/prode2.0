package PelusaDev.Prode.service;

import PelusaDev.Prode.model.Partido;

import java.util.List;
import java.util.Optional;

public interface IPartidoService {

    List<Partido> findAll();

    Optional<Partido> findById(Long id);

    Partido save(Partido partido);

    void deleteById(Long id);

    List<Partido> findByFechaId(Long fechaId);
}