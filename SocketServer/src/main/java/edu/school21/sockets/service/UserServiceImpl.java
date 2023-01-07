package edu.school21.sockets.service;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessageRepository;
import edu.school21.sockets.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import edu.school21.sockets.models.User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserServiceImpl implements UsersService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;

    @Autowired
    public UserServiceImpl(UserRepository usersRepository,
                           MessageRepository messageRepository) {
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.userRepository = usersRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public void signUp(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username: " + user.getUsername() + " already registered!");
        }
        userRepository.save(new User(user.getUsername(), passwordEncoder.encode(user.getPassword())));
    }

    @Override
    public boolean signIn(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent() && passwordEncoder.matches(password, user.get().getPassword());
    }

    @Override
    public void createMessage(String username, String roomTitle, String message) {
        User user = userRepository.findByUsername(username).get();
        messageRepository.save(new Message(user, message, roomTitle, LocalDateTime.now()));
    }

    @Override
    public String getLastRoom(String username) {
        return userRepository.findLastRoomByUsername(username);
    }

    @Override
    public void updateLastRoom(String username, String lastRoom) {
        userRepository.updateLastRoom(username, lastRoom);
    }
}