package LIVTech.authentication.authentication.service;

import LIVTech.authentication.authentication.Role.Role;
import LIVTech.authentication.authentication.controller.AuthenticationRequest;
import LIVTech.authentication.authentication.controller.AuthenticationResponse;
import LIVTech.authentication.authentication.controller.RegisterRequest;
import LIVTech.authentication.authentication.models.User;
import LIVTech.authentication.authentication.config.jwt.JwtService;
import LIVTech.authentication.authentication.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists!");
        }

        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(user);
        var token = jwtService.generateToken(user);

        return ResponseEntity.ok(AuthenticationResponse.builder()
                .message("User registered successfully!")
                .token(token)
                .build());
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail()).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
            }

            var token = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder()
                    .message("Authentication successful!")
                    .token(token)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password!");
        }
    }

    public ResponseEntity<?> resetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");
        }

        User user = userOptional.get();
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // Uncomment and implement the email-sending functionality as needed
        // sendResetEmail(email, token);

        return ResponseEntity.ok("Reset token has been sent to your email!");
    }

    public ResponseEntity<?> updatePassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired reset token!");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null); // Clear the reset token after password update
        userRepository.save(user);

        return ResponseEntity.ok("Password updated successfully!");
    }

    // Uncomment this method if you implement email functionality
//    private void sendResetEmail(String email, String token) {
//        String subject = "Password Reset Request";
//        String message = String.format(
//                "Hello,\n\nYou have requested to reset your password. Please use the following token to reset it:\n\n%s\n\nIf you did not request a password reset, please ignore this email.",
//                token
//        );
//
//        SimpleMailMessage mailMessage = new SimpleMailMessage();
//        mailMessage.setTo(email);
//        mailMessage.setSubject(subject);
//        mailMessage.setText(message);
//        mailMessage.setFrom("elijahmottey5@gmail.com");
//
//        mailSender.send(mailMessage);
//    }
}
