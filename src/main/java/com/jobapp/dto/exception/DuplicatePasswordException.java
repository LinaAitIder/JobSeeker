package com.jobapp.dto.exception;

public class DuplicatePasswordException extends RuntimeException {
    public DuplicatePasswordException() {
        super("Ce mot de passe est déjà utilisé par un autre utilisateur");
    }
}
