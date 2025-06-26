package PelusaDev.Prode.controller;

public record  AuthRequestDto (
        String nombre,
        String username,
        String password,
        String email
){}