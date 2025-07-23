package PelusaDev.Prode.service;

import PelusaDev.Prode.model.Partido;
import PelusaDev.Prode.model.Ticket;
import PelusaDev.Prode.model.Pronostico;
import PelusaDev.Prode.repository.PartidoRepository;
import PelusaDev.Prode.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PartidoService implements IPartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private TicketRepository ticketRepository;

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

    @Override
    public List<Partido> findByFechaId(Long fechaId) {
        return partidoRepository.findByFechaId(fechaId);
    }

    @Override
    public void recalcularTicketsDelPartido(Long partidoId) {
        // Buscar todos los tickets que contengan pronósticos para este partido
        List<Ticket> ticketsAfectados = ticketRepository.findByPronosticosPartidoId(partidoId);
        
        for (Ticket ticket : ticketsAfectados) {
            int puntosTotales = 0;
            
            // Recalcular puntos de todos los pronósticos del ticket
            for (Pronostico pronostico : ticket.getPronosticos()) {
                // Usar el partido directamente desde la relación
                Partido partido = pronostico.getPartido();
                if (partido != null) {
                    int puntosPronostico = calcularPuntosPronostico(pronostico, partido);
                    pronostico.setPuntosObtenidos(puntosPronostico);
                    puntosTotales += puntosPronostico;
                }
            }
            
            ticket.setPuntosTotales(puntosTotales);
            ticketRepository.save(ticket);
        }
    }

    private int calcularPuntosPronostico(Pronostico pronostico, Partido partido) {
        // Solo calcular puntos si el partido está finalizado Y tiene goles definidos
        if (partido.getGolesLocal() != null && partido.getGolesVisitante() != null && partido.getFinalizado()) {
            String resultadoReal;
            if (partido.getGolesLocal() > partido.getGolesVisitante()) {
                resultadoReal = "LOCAL";
            } else if (partido.getGolesLocal() < partido.getGolesVisitante()) {
                resultadoReal = "VISITANTE";
            } else {
                resultadoReal = "EMPATE";
            }
            
            // Verificar si el pronóstico coincide con el resultado real
            if (pronostico.getResultadoPronosticado().equals(resultadoReal)) {
                return 1; // Puedes ajustar la lógica de puntaje aquí
            } else {
                return 0;
            }
        } else {
            // Si el partido no está finalizado, no asignar puntos
            return 0;
        }
    }
}