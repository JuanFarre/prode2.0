package PelusaDev.Prode.service;

import PelusaDev.Prode.dto.TicketDTO;
import java.util.List;

public interface ITicketService {
    TicketDTO saveTicket(TicketDTO ticketDTO);
    List<TicketDTO> getAllTickets();
    TicketDTO getTicketById(Long id);
    void deleteTicket(Long id);
    List<TicketDTO> getTicketsByUsuarioId(Long usuarioId);
}
