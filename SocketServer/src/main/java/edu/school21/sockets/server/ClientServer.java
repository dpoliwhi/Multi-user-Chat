package edu.school21.sockets.server;

import edu.school21.sockets.json.JSONConverter;
import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.Message;
import edu.school21.sockets.models.User;
import edu.school21.sockets.service.MessageService;
import edu.school21.sockets.service.UsersService;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class ClientServer extends Thread {
    private final UsersService usersService;
    private final MessageService messageService;
    public PrintWriter output;
    private Scanner input;
    private Socket socket;
    private String username;
    private String password;
    private String roomTitle = "";
    public boolean isChatting;
    private User user;

    ClientServer(Socket socket, UsersService usersService, MessageService messageService) {
        this.usersService = usersService;
        this.messageService = messageService;
        try {
            this.socket = socket;
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void showMainMenu() {
        sendMsg("Choose command:");
        sendMsg("1. SignUp");
        sendMsg("2. SignIn");
        sendMsg("3. Exit");
    }

    @Override
    public void run() {
        sendMsg("Hello from Server!");
        label:
        while (true) {
            showMainMenu();
            try {
                String line = receiveMsg();
                switch (line) {
                    case "1":
                        if (!toSignUpClient()) break label;
                        break;
                    case "2":
                        if (!toSignInClient()) continue;
                        chooseOrCreateRoom();
                        break label;
                    case "3":
                    case "exit":
                        exitChat(username);
                        break label;
                    default:
                        sendMsg("Unknown command!");
                        break;
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage());
                sendMsg(e.getMessage());
            }
        }
    }

    public void showRoomMenu() {
        sendMsg("Choose command:");
        sendMsg("1. Create room");
        sendMsg("2. Choose room");
        sendMsg("3. Exit");
    }

    private void chooseOrCreateRoom() {
        while (true) {
            showRoomMenu();
            try {
                if (input.hasNextLine()) {
                    String message = receiveMsg();
                    switch (message) {
                        case "1":
                            createRoom();
                            toChatting();
                            return;
                        case "2":
                            chooseRoom();
                            return;
                        case "3":
                        case "exit":
                            log("User: '" + username + "' logged out");
                            sendMsg("Logout for " + username);
                            exitChat(username);
                            return;
                        default:
                            sendMsg("Unknown command!");
                            break;
                    }
                } else {
                    exitChat(username);
                    return;
                }
            } catch (RuntimeException e) {
                System.err.println(e.getMessage() + "!");
                sendMsg(e.getMessage());
            }
        }
    }

    private void createRoom() {
        sendMsg("Enter title of new chatroom");
        roomTitle = receiveMsg();
        while (roomTitle.isEmpty()) {
            roomTitle = receiveMsg();
        }
        Chatroom room = new Chatroom(roomTitle, username);
        MultiServer.roomsService.createRoom(room);
        MultiServer.rooms.add(roomTitle);
        log("User '" + username + "' created room: " + roomTitle);
        sendMsg("Room '" + roomTitle + "' created!");
    }

    private void chooseRoom() {
        List<Chatroom> allRooms = MultiServer.roomsService.showAllRooms();
        if (allRooms == null || allRooms.isEmpty()) {
            sendMsg("No rooms created!");
            chooseOrCreateRoom();
            return;
        } else {
            sendMsg("Rooms: ");
            for (int i = 0; i < allRooms.size(); i++) {
                sendMsg(i + 1 + ". " + allRooms.get(i).getTitle());
            }
            sendMsg(allRooms.size() + 1 + ". Exit");
        }
        int roomNumber;
        do {
            roomNumber = Integer.parseInt(receiveMsg());
            if (roomNumber <= allRooms.size() && roomNumber > 0) {
                roomTitle = allRooms.get(roomNumber - 1).getTitle();
                log("User '" + username + "' in room: '" + roomTitle + "'");
                toChatting();
                break;
            } else {
                sendMsg("Wrong number of room. Please, try again.");
            }
        } while (roomNumber - 1 != allRooms.size());
    }

    private boolean toSignInClient() {
        if (!getUserPass()) {
            exitChat(username);
            return false;
        }
        if (usersService.signIn(username, password)) {
            sendMsg("Authorization successful!");
            log("Authorization successful for user: " + username);
            return true;
        } else {
            sendMsg("Authorization failed!");
            log("Authorization failed for user: " + username);
        }
        return false;
    }

    private boolean toSignUpClient() {
        if (!getUserPass()) {
            exitChat("someone");
            return false;
        }
        usersService.signUp(new User(username, password));
        sendMsg("User: " + username + " created!");
        return true;
    }

    private boolean getUserPass() {
        sendMsg("Enter username: ");
        username = receiveMsg();
        while (username.isEmpty()) {
            username = receiveMsg();
        }
        if ("exit".equals(username)) return false;

        sendMsg("Enter password: ");
        password = receiveMsg();
        while (password.isEmpty()) {
            password = receiveMsg();
        }
        if ("exit".equals(password)) return false;
        user = new User(username, password);
        return true;
    }

    private void toChatting() {
        MultiServer.sendInfoMessage(username, roomTitle, "in chat!");
        sendMsg("Start messaging");

        String lastRoom = usersService.getLastRoom(username);
        if (lastRoom != null && lastRoom.equals(roomTitle)) {
            sendMsg("______________________________________________");
            sendMsg("Last messages:");
            showLastThirtyMessage();
            sendMsg("______________________________________________");
        }
        usersService.updateLastRoom(username, roomTitle);
        while (true) {
            isChatting = true;
            String message = receiveMsg();
            if ("exit".equals(message)) {
                exitChat(username);
                break;
            }
            if (!message.isEmpty())
                MultiServer.sendMessageToAll(username, roomTitle, message);
        }
    }

    private void showLastThirtyMessage() {
        List<Message> allMessage = messageService.getLastMsgInRoom(roomTitle);
        if (!allMessage.isEmpty()) {
            for (Message msg : allMessage) {
                sendMsg(msg.getSender().getUsername() + ": " + msg.getMessage());
            }
        }
    }

    private void exitChat(String username) {
        try {
            MultiServer.sendInfoMessage(username, roomTitle, " left the chat! ");
            MultiServer.removeClient(this);
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public String getUsername() {
        return username;
    }

    public String getRoomTitle() {
        return roomTitle;
    }

    protected void sendMsg(String text) {
        output.println(Objects.requireNonNull(JSONConverter.makeJSONObject(text, LocalDateTime.now())).toJSONString());
    }

    private String receiveMsg() {
        return JSONConverter.parseToObject(input.nextLine().trim()).getMessage();
    }

    private void log(String text) {
        System.out.println(text);
    }
}