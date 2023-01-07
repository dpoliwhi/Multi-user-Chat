package edu.school21.sockets.models;

import lombok.*;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "message")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    @Id
    @Setter(AccessLevel.NONE)
    @Column(name = "msg_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_seq")
    @SequenceGenerator(name = "message_seq", sequenceName = "SEQ_MESSAGE", allocationSize = 1)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "sender",
            foreignKey = @ForeignKey(name = "FK_MSG_SENDER")
    )
    private User sender;

    @Column(name = "message")
    private String message;

    @Column(name = "title_room")
    private String titleRoom;

    @Column(name = "time")
    private LocalDateTime time;

    public Message(User sender, String message, String titleRoom, LocalDateTime time) {
        this.sender = sender;
        this.message = message;
        this.titleRoom = titleRoom;
        this.time = time;
    }
}