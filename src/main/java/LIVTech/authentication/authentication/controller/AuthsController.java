package LIVTech.authentication.authentication.controller;

import LIVTech.authentication.authentication.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auths")
@RequiredArgsConstructor
public class AuthsController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest request
    ) {
        return authenticationService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody AuthenticationRequest request
    ) {
        return authenticationService.authenticate(request);
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String email) {
        return authenticationService.resetPassword(email);
    }

    @PostMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestParam String token,
            @RequestParam String newPassword
    ) {
        return authenticationService.updatePassword(token, newPassword);
    }
}
