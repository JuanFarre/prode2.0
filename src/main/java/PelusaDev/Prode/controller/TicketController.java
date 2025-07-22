package PelusaDev.Prode.controller;

import PelusaDev.Prode.dto.TicketDTO;
import PelusaDev.Prode.service.ITicketService;
import PelusaDev.Prode.utils.JwtUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private ITicketService ticketService;

    // Nuevo endpoint para depuración
    @GetMapping("/debug-auth")
    public ResponseEntity<Map<String, Object>> debugAuth(HttpServletRequest request) {
        Map<String, Object> debugInfo = new HashMap<>();
        
        // Información del token
        String authHeader = request.getHeader("Authorization");
        debugInfo.put("hasAuthHeader", authHeader != null);
        debugInfo.put("authHeaderStartsWithBearer", authHeader != null && authHeader.startsWith("Bearer "));
        
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            debugInfo.put("tokenLength", token.length());
            debugInfo.put("tokenValid", JwtUtils.validateToken(token));
            
            // Extraer información del token
            JwtUtils.getUsernameFromToken(token).ifPresent(username -> debugInfo.put("username", username));
            JwtUtils.getUserIdFromToken(token).ifPresent(userId -> debugInfo.put("userId", userId));
            
            // Verificar roles
            debugInfo.put("hasRoleUser", JwtUtils.hasRole(token, "USER"));
            debugInfo.put("hasRoleAdmin", JwtUtils.hasRole(token, "ADMIN"));
            debugInfo.put("hasRoleRoleUser", JwtUtils.hasRole(token, "ROLE_USER"));
            debugInfo.put("hasRoleRoleAdmin", JwtUtils.hasRole(token, "ROLE_ADMIN"));
        }
        
        // Información del contexto de seguridad
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            debugInfo.put("securityContextAuth", auth.getName());
            debugInfo.put("securityContextAuthorities", auth.getAuthorities().toString());
            debugInfo.put("securityContextAuthenticated", auth.isAuthenticated());
        } else {
            debugInfo.put("securityContextAuth", "No authentication in SecurityContext");
        }
        
        return ResponseEntity.ok(debugInfo);
    }

    @PostMapping("/create")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO, HttpServletRequest request) {
        // Obtener el ID del usuario desde el token JWT
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        // Establecer el ID del usuario en el ticketDTO
        ticketDTO.setUsuarioId(userId);
        
        TicketDTO saved = ticketService.saveTicket(ticketDTO);
        return ResponseEntity.ok(saved);
    }    
    
    @GetMapping
    public ResponseEntity<List<TicketDTO>> getMyTickets(HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.ok(ticketService.getTicketsByUsuarioId(userId));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN') or hasRole('USER') or hasRole('ADMIN') or isAuthenticated()")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        // Imprimir información de depuración
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            System.out.println("Usuario autenticado: " + auth.getName());
            System.out.println("Autoridades: " + auth.getAuthorities());
            System.out.println("¿Está autenticado? " + auth.isAuthenticated());
        } else {
            System.out.println("No hay autenticación en el SecurityContext");
        }
        
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        TicketDTO ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Verificar que el ticket pertenezca al usuario que hace la solicitud
        if (!ticket.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        return ResponseEntity.ok(ticket);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id, HttpServletRequest request) {
        Long userId = getUserIdFromRequest(request);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        TicketDTO ticket = ticketService.getTicketById(id);
        if (ticket == null) {
            return ResponseEntity.notFound().build();
        }
        
        // Para administradores, permitir eliminar cualquier ticket
        // Para usuarios normales, verificar que el ticket les pertenezca
        if (!hasAdminRole(request) && !ticket.getUsuarioId().equals(userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Método privado para extraer el ID del usuario del token JWT en la solicitud
     */
    private Long getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            Optional<Long> userIdOpt = JwtUtils.getUserIdFromToken(token);
            return userIdOpt.orElse(null);
        }
        return null;
    }
    
    /**
     * Método privado para verificar si el usuario tiene rol de administrador
     */
    private boolean hasAdminRole(HttpServletRequest request) {
        // Esta implementación depende de cómo esté configurado JwtUtils
        // Aquí se asume que existe un método para verificar roles
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            return JwtUtils.hasRole(token, "ROLE_ADMIN");
        }
        return false;
    }
}
