package com.jobapp.dto.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("L'email " + email + " est déjà utilisé");
    }
}