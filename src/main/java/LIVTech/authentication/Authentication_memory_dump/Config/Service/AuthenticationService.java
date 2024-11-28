package LIVTech.authentication.Authentication_memory_dump.Config.Service;

import LIVTech.authentication.Authentication_memory_dump.Config.Models.User;
import LIVTech.authentication.Authentication_memory_dump.Config.Repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final String SECRET_KEY = System.getenv("JWT_SECRET_KEY");  // Store secret in env variables

    /**
     * Handles user registration by creating a new user with the provided email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return a message confirming the registration
     */
    public String signUp(String email, String password) {
        // Check if user already exists
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists!");
        }

        // Create new user and save to repository
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);

        return "User registered successfully!";
    }

    /**
     * Handles user login by validating email and password.
     *
     * @param email the user's email address
     * @param password the user's password
     * @return a JWT token for authenticated users
     */
    public String login(String email, String password) {
        // Retrieve user by email
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty() || !passwordEncoder.matches(password, userOptional.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials!");
        }

        // Generate JWT token upon successful authentication
        return generateJwtToken(email);
    }

    /**
     * Generates a JWT token for a user.
     *
     * @param email the user's email address
     * @return the JWT token
     */
    private String generateJwtToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 864_000_00))  // 1 day expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * Handles the password reset process by generating a reset token and associating it with the user.
     *
     * @param email the user's email address
     * @return a message containing the reset token
     */
    public String resetPassword(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }

        // Generate a unique reset token and associate it with the user
        String token = UUID.randomUUID().toString();
        User user = userOptional.get();
        user.setResetToken(token);
        userRepository.save(user);

        // Ideally, you would send this token via email
        return "Reset token: " + token;
    }

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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid reset token!");
        }

        User user = userOptional.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);  // Clear the reset token after password update
        userRepository.save(user);

        return "Password updated successfully!";
    }
}
