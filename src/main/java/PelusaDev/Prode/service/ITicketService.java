package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.TicketDTO;
import java.util.List;
import java.util.Map;

public interface ITicketService {
    TicketDTO saveTicket(TicketDTO ticketDTO);
    List<TicketDTO> getAllTickets();
    TicketDTO getTicketById(Long id);
    void deleteTicket(Long id);
    List<TicketDTO> getTicketsByUsuarioId(Long usuarioId);
    boolean existeTicketParaUsuarioYFecha(Long usuarioId, Long fechaId);
    
    // Nuevos métodos para ranking
    List<Map<String, Object>> getRankingGeneral();
    List<Map<String, Object>> getRankingPorFecha(Long fechaId);
}
