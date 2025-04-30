package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.RankingDTO;
import PelusaDev.Prode.model.Partido;
import PelusaDev.Prode.model.Pronostico;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.repository.PartidoRepository;
import PelusaDev.Prode.repository.PronosticoRepository;
import PelusaDev.Prode.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PronosticoService implements IPronosticoService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PronosticoRepository pronosticoRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    public void calcularPuntos(Long partidoId) {
        List<Pronostico> pronosticos = pronosticoRepository.findByPartidoId(partidoId);

        for (Pronostico pronostico : pronosticos) {
            Partido partido = pronostico.getPartido();
            int puntos = calcularPuntosPronostico(pronostico.getResultadoPronosticado(), partido);
            pronostico.setPuntosObtenidos(puntos);
            pronosticoRepository.save(pronostico);
            actualizarPuntosTotales(pronostico.getUsuario().getId());
        }
    }

    public void actualizarPuntosTotales(Long usuarioId) {
        List<Pronostico> pronosticos = pronosticoRepository.findByUsuarioId(usuarioId);

        int totalPuntos = pronosticos.stream()
                .mapToInt(Pronostico::getPuntosObtenidos)
                .sum();

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setPuntosTotales(totalPuntos);
        usuarioRepository.save(usuario);
    }

    private int calcularPuntosPronostico(String resultadoPronosticado, Partido partido) {
        if (partido.getGolesLocal() == null || partido.getGolesVisitante() == null) {
            return 0;
        }

        String resultadoReal = partido.getGolesLocal() > partido.getGolesVisitante() ? "LOCAL" :
                partido.getGolesLocal() < partido.getGolesVisitante() ? "VISITANTE" : "EMPATE";

        return resultadoPronosticado.equals(resultadoReal) ? 1 : 0;
    }

    @Override
    public List<Pronostico> findAll() {
        return pronosticoRepository.findAll();
    }

    @Override
    public Optional<Pronostico> findById(Long id) {
        return pronosticoRepository.findById(id);
    }

    @Override
    public Pronostico save(Pronostico pronostico) {
        Usuario usuario = usuarioRepository.findById(pronostico.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + pronostico.getUsuario().getId()));

        Partido partido = partidoRepository.findById(pronostico.getPartido().getId())
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado con ID: " + pronostico.getPartido().getId()));

        Pronostico nuevoPronostico = new Pronostico();
        nuevoPronostico.setUsuario(usuario);
        nuevoPronostico.setPartido(partido);
        nuevoPronostico.setResultadoPronosticado(pronostico.getResultadoPronosticado());

        int puntos = calcularPuntosPronostico(nuevoPronostico.getResultadoPronosticado(), partido);
        nuevoPronostico.setPuntosObtenidos(puntos);

        Pronostico guardado = pronosticoRepository.save(nuevoPronostico);

        // Solo actualiza los puntos totales si ya hay resultado del partido
        if (partido.getGolesLocal() != null && partido.getGolesVisitante() != null) {
            actualizarPuntosTotales(usuario.getId());
        }

        return guardado;
    }

    @Override
    public void deleteById(Long id) {
        pronosticoRepository.deleteById(id);
    }

    @Override
    public List<Pronostico> findByPartidoFechaId(Long fechaId) {
        return pronosticoRepository.findByPartidoFechaId(fechaId);
    }

    @Override
    public List<Pronostico> findByUsuarioAndFecha(Long usuarioId, Long fechaId) {
        return pronosticoRepository.findByUsuarioAndFecha(usuarioId, fechaId);
    }

    public List<RankingDTO> getRankingPorFecha(Long fechaId) {
        List<Pronostico> pronosticos = pronosticoRepository.findByPartidoFechaId(fechaId);

        Map<Long, Integer> puntosPorUsuario = new HashMap<>();

        for (Pronostico p : pronosticos) {
            Long usuarioId = p.getUsuario().getId();
            puntosPorUsuario.put(
                    usuarioId,
                    puntosPorUsuario.getOrDefault(usuarioId, 0) + p.getPuntosObtenidos()
            );
        }

        List<RankingDTO> ranking = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : puntosPorUsuario.entrySet()) {
            Long usuarioId = entry.getKey();
            String username = usuarioRepository.findById(usuarioId)
                    .map(Usuario::getUsername)
                    .orElse("Desconocido");

            ranking.add(new RankingDTO(usuarioId, username, entry.getValue()));
        }

        ranking.sort(Comparator.comparingInt(RankingDTO::getTotalPuntos).reversed());

        return ranking;
    }
}