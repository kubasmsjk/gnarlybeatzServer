package pl.gnarlybeatz.gnarlybeatzServer.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("SELECT e.email FROM User e WHERE e.id = :id")
    String findUserEmailByUserId(@Param("id") String id);

}
