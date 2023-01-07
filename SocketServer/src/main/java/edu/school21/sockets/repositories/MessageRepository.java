package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query(value = "SELECT * " +
            "FROM (SELECT * FROM message WHERE message.title_room = :roomTitle ORDER BY time DESC LIMIT 30) as m\n" +
            "ORDER BY time ASC", nativeQuery = true)
    List<Message> findMessageByTitleRoomOrderByTime(String roomTitle);
}
