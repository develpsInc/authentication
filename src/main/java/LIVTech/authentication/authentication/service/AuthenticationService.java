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
    //private final JavaMailSender mailSender;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = userRepository.findByEmail(request.getEmail()).orElse(null);
        var token = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }


    /**
     * Handles the password reset process by generating a reset token and associating it with the user.
     *
     * @param email the user's email address
     * @return a message confirming the reset token generation
     */
    public String resetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        User user = userOptional.get();

        // Generate a unique reset token
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        userRepository.save(user);

        // Send the token via email
        //sendResetEmail(email, token);

        return "Reset token has been sent to your email!";
    }

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
//        mailSender.send(mailMessage); // Send the email
//    }

    /**
     * Updates the user's password using the reset token.
     *
     * @param token the reset token
     * @param newPassword the new password to set for the user
     * @return a message confirming the password update
     */
    public String updatePassword(String token, String newPassword) {
        Optional<User> userOptional = userRepository.findByResetToken(token);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid or expired reset token!");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);  // Clear the reset token after password update
        userRepository.save(user);

        return "Password updated successfully!";
    }

}
