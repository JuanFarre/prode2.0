package PelusaDev.Prode.controller;

public record AuthResponseDto (
        String token,
        AuthenticationStatus authStatus,
        String message
){
}
