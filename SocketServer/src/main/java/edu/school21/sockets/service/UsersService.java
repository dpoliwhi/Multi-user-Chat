package edu.school21.sockets.service;

import edu.school21.sockets.models.User;

public interface UsersService {
    void signUp(User user);

    boolean signIn(String username, String password);

    void createMessage(String username, String roomTitle, String message);

    String getLastRoom(String username);

    void updateLastRoom(String username, String lastRoom);
}
