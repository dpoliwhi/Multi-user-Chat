package edu.school21.sockets.service;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessageService {
    void saveMessage(String username, String roomTitle, String message);

    public List<Message> getLastMsgInRoom(String roomTitle);
}
