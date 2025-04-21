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
            String resultadoReal = partido.getGolesLocal() > partido.getGolesVisitante() ? "LOCAL" :
                    partido.getGolesLocal() < partido.getGolesVisitante() ? "VISITANTE" : "EMPATE";

            // Verificar si el pronóstico es correcto
            if (pronostico.getResultadoPronosticado().equals(resultadoReal)) {
                pronostico.setPuntosObtenidos(1); // Asignar 1 punto si acierta
            } else {
                pronostico.setPuntosObtenidos(0); // Asignar 0 puntos si no acierta
            }

            // Guardar el pronóstico actualizado
            pronosticoRepository.save(pronostico);
        }
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
        // Buscar el usuario por ID
        Usuario usuario = usuarioRepository.findById(pronostico.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con ID: " + pronostico.getUsuario().getId()));

        // Buscar el partido por ID
        Partido partido = partidoRepository.findById(pronostico.getPartido().getId())
                .orElseThrow(() -> new IllegalArgumentException("Partido no encontrado con ID: " + pronostico.getPartido().getId()));

        // Crear el pronóstico con los datos proporcionados
        Pronostico nuevoPronostico = new Pronostico();
        nuevoPronostico.setUsuario(usuario);
        nuevoPronostico.setPartido(partido);
        nuevoPronostico.setResultadoPronosticado(pronostico.getResultadoPronosticado());

        // Calcular puntos obtenidos si el partido tiene resultado
        if (partido.getGolesLocal() != null && partido.getGolesVisitante() != null) {
            String resultadoReal = partido.getGolesLocal() > partido.getGolesVisitante() ? "LOCAL" :
                    partido.getGolesLocal() < partido.getGolesVisitante() ? "VISITANTE" : "EMPATE";

            if (nuevoPronostico.getResultadoPronosticado().equals(resultadoReal)) {
                nuevoPronostico.setPuntosObtenidos(1);
            } else {
                nuevoPronostico.setPuntosObtenidos(0);
            }
        } else {
            nuevoPronostico.setPuntosObtenidos(0);
        }

        // Guardar el pronóstico
        return pronosticoRepository.save(nuevoPronostico);
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
        return pronosticoRepository.findByUsuarioAndFecha(usuarioId,fechaId);
    }

    public List<RankingDTO> getRankingPorFecha(Long fechaId) {
        // 1. Traer todos los pronósticos de la fecha
        List<Pronostico> pronosticos = pronosticoRepository.findByPartidoFechaId(fechaId);

        // 2. Agrupar por usuario y sumar puntos
        Map<Long, Integer> puntosPorUsuario = new HashMap<>();

        for (Pronostico p : pronosticos) {
            Long usuarioId = p.getUsuario().getId();
            puntosPorUsuario.put(
                    usuarioId,
                    puntosPorUsuario.getOrDefault(usuarioId, 0) + p.getPuntosObtenidos()
            );
        }

        // 3. Armar la lista de RankingDTO
        List<RankingDTO> ranking = new ArrayList<>();

        for (Map.Entry<Long, Integer> entry : puntosPorUsuario.entrySet()) {
            Long usuarioId = entry.getKey();
            String username = usuarioRepository.findById(usuarioId)
                    .map(u -> u.getUsername())
                    .orElse("Desconocido");

            ranking.add(new RankingDTO(usuarioId, username, entry.getValue()));
        }

        // 4. Ordenar de mayor a menor por puntos
        ranking.sort(Comparator.comparingInt(RankingDTO::getTotalPuntos).reversed());

        return ranking;
    }
}