package PelusaDev.Prode.service;

import PelusaDev.Prode.model.Torneo;
import PelusaDev.Prode.repository.TorneoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TorneoService implements ITorneoService {

    @Autowired
    private TorneoRepository torneoRepository;

    public List<Torneo> findAll() {
        return torneoRepository.findAll();
    }

    public Optional<Torneo> findById(Long id) {
        return torneoRepository.findById(id);
    }

    public Torneo save(Torneo torneo) {
        return torneoRepository.save(torneo);
    }

    public void deleteById(Long id) {
        torneoRepository.deleteById(id);
    }
}