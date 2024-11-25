package LIVTech.authentication.Controller;
import LIVTech.authentication.Service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthsController {

        @Autowired
        private AuthenticationService authService;

        @PostMapping("/signup")
        public String signUp(@RequestParam String email, @RequestParam String password) {
            return authService.signUp(email, password);
        }

        @PostMapping("/login")
        public String login(@RequestParam String email, @RequestParam String password) {
            return authService.login(email, password);
        }

        @PostMapping("/reset-password")
        public String resetPassword(@RequestParam String email) {
            return authService.resetPassword(email);
        }

        @PostMapping("/update-password")
        public String updatePassword(@RequestParam String token, @RequestParam String newPassword) {
            return authService.updatePassword(token, newPassword);
        }
    }


