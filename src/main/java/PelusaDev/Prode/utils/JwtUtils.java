package PelusaDev.Prode.utils;
import jakarta.annotation.PostConstruct;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class JwtUtils {

    private static SecretKey secretKey;
    private static final String ISSUER = "server";

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    private JwtUtils() {
    }

    public static boolean validateToken(String jwtToken) {
        return parseToken(jwtToken).isPresent();
    }

    private static Optional<Claims> parseToken(String jwtToken) {
        var jwtParser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        try {
            return Optional.of(jwtParser.parseSignedClaims(jwtToken).getPayload());
        } catch (JwtException | IllegalArgumentException e) {
            System.err.println("JWT Exception occurred: " + e.getMessage());
        }

        return Optional.empty();
    }

    public static Optional<String> getUsernameFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken);
        return claimsOptional.map(Claims::getSubject);
    }

    public static Optional<String> getRolesFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken);
        return claimsOptional.map(claims -> claims.get("roles", String.class));
    }    public static String generateToken(String username, String rol, Long userId) {
        var currentDate = new Date();
        var jwtExpirationInMinutes = 10;
        var expiration = DateUtils.addMinutes(currentDate, jwtExpirationInMinutes);

        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .issuer(ISSUER)
                .subject(username)
                .claim("rol", rol) // Agregar el rol como un claim
                .claim("userId", userId) // Agregar el ID del usuario como un claim
                .signWith(secretKey)
                .issuedAt(currentDate)
                .expiration(expiration)
                .compact();
    }

    public static Optional<Long> getUserIdFromToken(String jwtToken) {
        var claimsOptional = parseToken(jwtToken);
        return claimsOptional.map(claims -> claims.get("userId", Long.class));
    }
    
    /**
     * Verifica si el token contiene un rol específico
     * @param jwtToken El token JWT
     * @param role El rol a verificar
     * @return true si el token contiene el rol especificado, false en caso contrario
     */
    public static boolean hasRole(String jwtToken, String role) {
        var claimsOptional = parseToken(jwtToken);
        return claimsOptional
                .map(claims -> {
                    String userRole = claims.get("rol", String.class);
                    // Verifica tanto el rol directo como con el prefijo ROLE_
                    return role.equals(userRole) || ("ROLE_" + userRole).equals(role);
                })
                .orElse(false);
    }
}