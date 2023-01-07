package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query("SELECT lastRoom FROM User WHERE username = :username")
    String findLastRoomByUsername(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User SET lastRoom = :lastRoom  WHERE username = :username")
    void updateLastRoom(@Param("username") String username, @Param("lastRoom") String lastRoom);
}
