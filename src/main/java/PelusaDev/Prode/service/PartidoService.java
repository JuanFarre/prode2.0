package PelusaDev.Prode.service;

import PelusaDev.Prode.model.Partido;
import PelusaDev.Prode.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidoService implements IPartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    @Override
    public List<Partido> findAll() {
        return partidoRepository.findAll();
    }

    @Override
    public Optional<Partido> findById(Long id) {
        return partidoRepository.findById(id);
    }

    @Override
    public Partido save(Partido partido) {
        return partidoRepository.save(partido);
    }

    @Override
    public void deleteById(Long id) {
        partidoRepository.deleteById(id);
    }
}