package edu.school21.sockets.service;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomsServiceImpl implements RoomsService {
    private final RoomRepository roomRepository;

    @Autowired
    public RoomsServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public void createRoom(Chatroom room) {
        if (roomRepository.findByTitle(room.getTitle()).isPresent()) {
            throw new RuntimeException("Room: " + room.getTitle() + " already exist");
        }
        roomRepository.save(room);
    }

    @Override
    public List<Chatroom> showAllRooms() {
        return roomRepository.findAll();
    }
}
