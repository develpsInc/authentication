package LIVTech.authentication.Dashbord.dashboardController;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/dashboard")
public class DashboardController {

    @GetMapping("/user_info")
    public ResponseEntity<String> demo() {
        return ResponseEntity.ok("Hello World");
    }
}
