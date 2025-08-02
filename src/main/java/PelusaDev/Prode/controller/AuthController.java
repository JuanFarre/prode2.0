package PelusaDev.Prode.controller;

import PelusaDev.Prode.service.AuthServiceImpl;
import PelusaDev.Prode.service.IAuthService;
import PelusaDev.Prode.token.VerificationToken;
import PelusaDev.Prode.token.VerificationTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
// Indica que esta clase es un controlador en una aplicación REST y que los métodos devuelven directamente los datos al cliente (en formato JSON, por ejemplo).
@RequestMapping("/api/auth") // Define la ruta base para los endpoints de este controlador: "/api/auth".
@RequiredArgsConstructor
@CrossOrigin
public class AuthController {

    @Value("${app.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    @Autowired
    private IAuthService authService; // Inyección de dependencia del servicio encargado de la lógica de autenticación.
    @Autowired
    private VerificationTokenRepository tokenRepository;

    @Autowired
    private AuthServiceImpl userService;

    /**
     * Endpoint para el inicio de sesión (login).
     *
     * @param authRequestDto Contiene el nombre de usuario y la contraseña
     * proporcionados por el cliente.
     * @return Respuesta HTTP con un token JWT y el estado de autenticación.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@RequestBody AuthRequestDto authRequestDto) {
        try {            // Autenticar al usuario y generar el token JWT
            var jwtToken = authService.login(authRequestDto.username(), authRequestDto.password());

            // Crear la respuesta simplificada
            var authResponseDto = new AuthResponseDto(
                    jwtToken,
                    AuthenticationStatus.LOGIN_SUCCESS,
                    "Usuario autenticado con éxito"
            );

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResponseDto);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthenticationStatus status = AuthenticationStatus.LOGIN_FAILED;

            if (errorMessage.contains("Usuario no encontrado")) {
                errorMessage = "Usuario no encontrado";
            } else if (errorMessage.contains("La cuenta no ha sido verificada")) {
                errorMessage = "La cuenta no ha sido verificada. Por favor, revise su correo electrónico.";
            } else if (errorMessage.contains("Bad credentials")) {
                errorMessage = "Usuario o contraseña incorrectos";            }
            
            var authResponseDto = new AuthResponseDto(null, status, errorMessage);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(authResponseDto);
        }
    }


    /**
     * Endpoint para registrar un nuevo usuario (sign-up).
     *
     * @param authRequestDto Contiene el nombre, nombre de usuario y contraseña
     * del nuevo usuario.
     * @return Respuesta HTTP con un token JWT si el registro fue exitoso, o un
     * mensaje de error si no.
     */
    @PostMapping("/registrar") // Define que este método manejará solicitudes POST
    public ResponseEntity<AuthResponseDto> signUp(@RequestBody AuthRequestDto authRequestDto) {
        try {            // Llama al servicio para registrar al usuario y generar un token JWT.
            var jwtToken = authService.signUp(authRequestDto.nombre(), authRequestDto.username(), authRequestDto.password(), authRequestDto.email());
            
            // Crea un objeto de respuesta con el token y el estado de éxito.
            var authResponseDto = new AuthResponseDto(jwtToken,
                    AuthenticationStatus.USER_CREATED_SUCCESSFULLY,
                    "Usuario registrado exitosamente");

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(authResponseDto);

        } catch (Exception e) {
            String errorMessage = e.getMessage();
            AuthenticationStatus status = AuthenticationStatus.USER_NOT_CREATED;            // Personalizar mensajes según el tipo de error
            if (e.getMessage().contains("Username already exists")) {
                errorMessage = "El nombre de usuario ya está en uso";
            } else if (e.getMessage().contains("Email already exists")) {
                errorMessage = "El correo electrónico ya está registrado";
            }
            
            var authResponseDto = new AuthResponseDto(null, status, errorMessage);

            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(authResponseDto);
        }
    }

    @GetMapping("/verifyEmail")
    public void verifyEmail(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        // Imprime el token recibido en la consola para verificación
        System.out.println("Recibida solicitud de verificación para token: " + token);
        System.out.println("Frontend URL configurada: " + frontendUrl);

        try {
            // Busca el token de verificación en la base de datos utilizando el repositorio
            VerificationToken theToken = tokenRepository.findByToken(token);

            // Si no se encuentra el token, redirige al usuario a la página de verificación con un mensaje de error
            if (theToken == null) {
                System.out.println("Token no encontrado");
                response.sendRedirect(frontendUrl + "/verification?status=invalid-token");
                return;
            }

            // Valida el token y guarda el resultado
            String result = userService.validateToken(token);

            // Imprime el resultado de la validación en la consola para saber si es válido, expirado, etc.
            System.out.println("Resultado de la validación: " + result);

            // Dependiendo del resultado de la validación, redirige al usuario a diferentes páginas
            switch (result) {
                case "valido":
                    // Si el token es válido, redirige con un mensaje de éxito
                    response.sendRedirect(frontendUrl + "/verification?status=success");
                    break;
                case "expired":
                    // Si el token ya ha expirado, redirige con un mensaje de expiración
                    response.sendRedirect(frontendUrl + "/verification?status=expired");
                    break;
                default:
                    // Si el token no es válido, redirige con un mensaje de error
                    response.sendRedirect(frontendUrl + "/verification?status=invalid-token");
            }
        } catch (Exception e) {
            // Si ocurre algún error durante el proceso de verificación, se captura la excepción
            // Se imprime el mensaje del error en la consola y se redirige al usuario a una página de error
            System.out.println("Error durante la verificación: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(frontendUrl + "/verification?status=error");
        }
    }

    public String applicationUrl(HttpServletRequest request) {
        // Construye la URL completa del servidor (incluye el nombre del servidor, puerto y contexto) para usarla en la verificación por correo
        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

}