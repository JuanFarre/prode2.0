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
                    if (partido.getGolesLocal() != null && partido.getGolesVisitante() != null) {
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
}
