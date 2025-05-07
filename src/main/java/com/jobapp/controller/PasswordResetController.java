package com.jobapp.controller;

import com.jobapp.dto.request.ResetPasswordRequest;
import com.jobapp.service.AuthHelperService;
import com.jobapp.service.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class PasswordResetController {
    private final PasswordResetService passwordResetService;
    private final AuthHelperService authHelperService;

    @Autowired
    public PasswordResetController(PasswordResetService passwordResetService,
                                   AuthHelperService authHelperService){
        this.passwordResetService = passwordResetService;
        this.authHelperService = authHelperService;
    }
    @PatchMapping("/password")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
       System.out.println(request);
        passwordResetService.resetPassword(
                request.email(),
                request.newPassword(),
                request.userType()
        );
        return ResponseEntity.ok().build();
    }
}
