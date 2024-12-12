package LIVTech.authentication.authentication.repository;

import LIVTech.authentication.authentication.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByResetToken(String resetToken);

    boolean existsByEmail(String email);
}
