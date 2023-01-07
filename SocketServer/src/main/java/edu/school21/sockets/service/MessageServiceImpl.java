package edu.school21.sockets.service;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.MessageRepository;

import edu.school21.sockets.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImpl implements MessageService {
    private MessageRepository messageRepository;
    private UserRepository userRepository;

    @Autowired
    private void setMessageRepository(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveMessage(String username, String roomTitle, String message) {

        Optional<User> sender = userRepository.findByUsername(username);
        Message msg = new Message(sender.get(), roomTitle, message, LocalDateTime.now());
        messageRepository.save(msg);
    }

    @Override
    public List<Message> getLastMsgInRoom(String roomTitle) {
        return messageRepository.findMessageByTitleRoomOrderByTime(roomTitle);
    }
}