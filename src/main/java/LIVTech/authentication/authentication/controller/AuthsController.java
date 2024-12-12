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
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody RegisterRequest request
    ){
        return ResponseEntity.ok(authenticationService.register(request));

    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> register (
            @RequestBody AuthenticationRequest request
    ){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


    @GetMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestParam String email) {
        return ResponseEntity.ok(authenticationService.resetPassword(email));
    }


//    @PostMapping("/reset-password")
//    public String resetPassword(@RequestParam String email) {
//
//        return authenticationService.resetPassword(email);
//    }

    @PostMapping("/update-password")
    public String updatePassword(@RequestParam String token, @RequestParam String newPassword) {
        return authenticationService.updatePassword(token, newPassword);
    }


}
