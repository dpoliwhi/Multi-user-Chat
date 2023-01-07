package edu.school21.sockets.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chatroom")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Chatroom {
    @Id
    @Setter(AccessLevel.NONE)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatroom_seq")
    @SequenceGenerator(name = "chatroom_seq", sequenceName = "SEQ_CHATROOM", allocationSize = 1)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "owner")
    private String owner;

    public Chatroom(String title, String owner) {
        this.title = title;
        this.owner = owner;
    }
}
