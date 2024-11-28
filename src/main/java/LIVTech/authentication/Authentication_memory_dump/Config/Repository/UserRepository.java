package LIVTech.authentication.Authentication_memory_dump.Config.Repository;

import LIVTech.authentication.Authentication_memory_dump.Config.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByResetToken(String resetToken);


}
