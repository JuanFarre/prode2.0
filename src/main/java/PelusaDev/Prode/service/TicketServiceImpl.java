package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.TicketDTO;
import PelusaDev.Prode.mapper.TicketMapper;
import PelusaDev.Prode.mapper.PronosticoMapper;
import PelusaDev.Prode.model.Ticket;
import PelusaDev.Prode.model.Usuario;
import PelusaDev.Prode.model.Fecha;
import PelusaDev.Prode.model.Pronostico;
import PelusaDev.Prode.repository.TicketRepository;
import PelusaDev.Prode.repository.UsuarioRepository;
import PelusaDev.Prode.repository.FechaRepository;
import PelusaDev.Prode.repository.PartidoRepository;
import PelusaDev.Prode.model.Partido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

@Service
public class TicketServiceImpl implements ITicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private FechaRepository fechaRepository;
    @Autowired
    private PartidoRepository partidoRepository;

    @Override
    public TicketDTO saveTicket(TicketDTO ticketDTO) {
        Ticket ticket = TicketMapper.toEntity(ticketDTO);
        Usuario usuario = usuarioRepository.findById(ticketDTO.getUsuarioId()).orElseThrow();
        Fecha fecha = fechaRepository.findById(ticketDTO.getFechaId()).orElseThrow();
        ticket.setUsuario(usuario);
        ticket.setFecha(fecha);
        if(ticketDTO.getPronosticos() != null) {
            List<Pronostico> pronosticos = ticketDTO.getPronosticos().stream()
                .map(dto -> {
                    Pronostico p = PronosticoMapper.toEntity(dto);
                    p.setTicket(ticket);
                    Partido partido = partidoRepository.findById(dto.getPartidoId()).orElseThrow();
                    p.setPartido(partido);
                    p.setUsuario(usuario);
                    // Traducción de resultado
                    switch (p.getResultadoPronosticado()) {
                        case "1":
                            p.setResultadoPronosticado("LOCAL");
                            break;
                        case "X":
                            p.setResultadoPronosticado("EMPATE");
                            break;
                        case "2":
                            p.setResultadoPronosticado("VISITANTE");
                            break;
                    }
                    // Calcular puntos obtenidos
                   if (partido.getGolesLocal() != null && partido.getGolesVisitante() != null && partido.getFinalizado()) {
                        String resultadoReal;
                        if (partido.getGolesLocal() > partido.getGolesVisitante()) {
                            resultadoReal = "LOCAL";
                        } else if (partido.getGolesLocal() < partido.getGolesVisitante()) {
                            resultadoReal = "VISITANTE";
                        } else {
                            resultadoReal = "EMPATE";
                        }
                        if (p.getResultadoPronosticado().equals(resultadoReal)) {
                            p.setPuntosObtenidos(1); // Puedes cambiar la lógica de puntaje aquí
                        } else {
                            p.setPuntosObtenidos(0);
                        }
                    } else {
                        p.setPuntosObtenidos(0);
                    }
                    return p;
                })
                .collect(Collectors.toList());
            ticket.setPronosticos(pronosticos);
            // Sumar puntos totales del ticket
            int puntosTotales = pronosticos.stream().mapToInt(Pronostico::getPuntosObtenidos).sum();
            ticket.setPuntosTotales(puntosTotales);
        }
        Ticket saved = ticketRepository.save(ticket);
        return TicketMapper.toDTO(saved);
    }

    @Override
    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll().stream().map(TicketMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public TicketDTO getTicketById(Long id) {
        return ticketRepository.findById(id).map(TicketMapper::toDTO).orElse(null);
    }

    @Override
    public void deleteTicket(Long id) {
        ticketRepository.deleteById(id);
    }

    @Override
    public List<TicketDTO> getTicketsByUsuarioId(Long usuarioId) {
        return ticketRepository.findByUsuarioId(usuarioId).stream()
                .map(TicketMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existeTicketParaUsuarioYFecha(Long usuarioId, Long fechaId) {
        return ticketRepository.existsByUsuarioIdAndFechaId(usuarioId, fechaId);
    }

    @Override
    public List<Map<String, Object>> getRankingGeneral() {
        List<Ticket> tickets = ticketRepository.findAll();
        return procesarTicketsParaRanking(tickets);
    }

    @Override
    public List<Map<String, Object>> getRankingPorFecha(Long fechaId) {
        List<Ticket> tickets = ticketRepository.findAll().stream()
            .filter(ticket -> ticket.getFecha().getId().equals(fechaId))
            .collect(Collectors.toList());
        return procesarTicketsParaRanking(tickets);
    }

    private List<Map<String, Object>> procesarTicketsParaRanking(List<Ticket> tickets) {
        // Agrupar tickets por usuario
        Map<Long, List<Ticket>> ticketsPorUsuario = tickets.stream()
            .collect(Collectors.groupingBy(ticket -> ticket.getUsuario().getId()));
        
        List<Map<String, Object>> ranking = new ArrayList<>();
        
        for (Map.Entry<Long, List<Ticket>> entry : ticketsPorUsuario.entrySet()) {
            Long usuarioId = entry.getKey();
            List<Ticket> ticketsUsuario = entry.getValue();
            
            // Calcular puntos totales del usuario
            int puntosTotales = ticketsUsuario.stream()
                .mapToInt(ticket -> ticket.getPuntosTotales() != null ? ticket.getPuntosTotales() : 0)
                .sum();
            
            // Encontrar el mejor ticket del usuario
            Ticket mejorTicket = ticketsUsuario.stream()
                .max((t1, t2) -> Integer.compare(
                    t1.getPuntosTotales() != null ? t1.getPuntosTotales() : 0,
                    t2.getPuntosTotales() != null ? t2.getPuntosTotales() : 0
                ))
                .orElse(ticketsUsuario.get(0));
            
            // Obtener el username del usuario
            String username = mejorTicket.getUsuario().getUsername();
            
            Map<String, Object> rankingItem = new HashMap<>();
            rankingItem.put("usuarioId", usuarioId);
            rankingItem.put("username", username);
            rankingItem.put("puntosTotales", puntosTotales);
            rankingItem.put("ticketId", mejorTicket.getId());
            
            ranking.add(rankingItem);
        }
        
        // Ordenar por puntos (de mayor a menor)
        ranking.sort((a, b) -> Integer.compare(
            (Integer) b.get("puntosTotales"), 
            (Integer) a.get("puntosTotales")
        ));
        
        // Asignar posiciones
        for (int i = 0; i < ranking.size(); i++) {
            ranking.get(i).put("posicion", i + 1);
        }
        
        return ranking;
    }
}
