package PelusaDev.Prode.controller;

public enum AuthenticationStatus {
    USER_CREATED_SUCCESSFULLY, // Indica que un nuevo usuario fue creado exitosamente.
    USER_NOT_CREATED, // Indica que hubo un problema al intentar crear un usuario.
    LOGIN_SUCCESS, // Indica que el inicio de sesión fue exitoso.
    LOGIN_FAILED               // Indica que el intento de inicio de sesión falló.
}