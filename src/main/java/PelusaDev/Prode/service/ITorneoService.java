package PelusaDev.Prode.service;

import PelusaDev.Prode.model.Torneo;

import java.util.List;
import java.util.Optional;

public interface ITorneoService {

    List<Torneo> findAll();

    Optional<Torneo> findById(Long id);

    Torneo save(Torneo torneo);

    void deleteById(Long id);
}