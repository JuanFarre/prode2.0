package PelusaDev.Prode.mapper;

import PelusaDev.Prode.dto.TicketDTO;
import PelusaDev.Prode.dto.PronosticoDTO;
import PelusaDev.Prode.model.Ticket;
import PelusaDev.Prode.model.Pronostico;
import java.util.stream.Collectors;

public class TicketMapper {
    public static TicketDTO toDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getUsuario().getId(),
                ticket.getFechaCreacion(),
                ticket.getFecha().getId(),
                ticket.getPronosticos() != null ?
                    ticket.getPronosticos().stream().map(PronosticoMapper::toDTO).collect(Collectors.toList()) : null,
                ticket.getPuntosTotales()
        );
    }

    public static Ticket toEntity(TicketDTO dto) {
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        // Usuario y Fecha deben ser seteados en el servicio usando los repositorios
        ticket.setFechaCreacion(dto.getFechaCreacion());
        ticket.setPuntosTotales(dto.getPuntosTotales());
        // Pronósticos deben ser mapeados y seteados en el servicio
        return ticket;
    }
}
