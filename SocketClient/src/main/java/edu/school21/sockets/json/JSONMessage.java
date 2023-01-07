package edu.school21.sockets.json;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class JSONMessage {
    private String message;
    private LocalDateTime time;
}
