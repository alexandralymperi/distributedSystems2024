package gr.hua.dit.ds.ds2024Team77.controllers;


import gr.hua.dit.ds.ds2024Team77.entities.*;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import gr.hua.dit.ds.ds2024Team77.repository.MessagesRepository;
import gr.hua.dit.ds.ds2024Team77.service.MessagesService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
public class MessagesController {

    private MessagesRepository mRepository;
    private MessagesService mService;
    private UserService userService;

    public MessagesController(MessagesRepository mRepository, MessagesService mService, UserService userService) {
        this.mRepository = mRepository;
        this.mService = mService;
        this.userService = userService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("")
    public ResponseEntity<?> getMessages(){

        try {
            List<Messages> messages = mService.getMessages();

            if (messages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages found.");
            }

            return ResponseEntity.ok(messages);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"}) //?????
    @GetMapping("/{messageId}")
    public ResponseEntity<Messages> getMessage(@PathVariable Long messageId){
        Optional<Messages> message = mService.getMessage(messageId);
        return message.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_BASIC"}) //correct
    @PostMapping("/send/{recipientId}")
    public ResponseEntity<String> sendMessage(
            @PathVariable Long recipientId,
            @RequestBody Messages message,
            @AuthenticationPrincipal UserDetailsImpl auth) {

        try{
            Long senderId = auth.getId();

            Optional<User> recipientOpt = userService.getUser(recipientId);
            if (recipientOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipient not found.");
            }

            message.setSender(userService.getUser(senderId).get());
            message.setReceiver(recipientOpt.get());

            mService.saveMessages(message);

            return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully.");

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @Secured({"ROLE_BASIC","ROLE_ADMIN"})
    @GetMapping("/conversation/{userId}") //correct
    public ResponseEntity<?> getConversation(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl auth) {

        try{
            Long loggedInUserId = auth.getId();

            List<Messages> allMessages = mService.getMessages();

            List<Messages> conversation = allMessages.stream()
                    .filter(msg ->
                            (msg.getSender().getId().equals(loggedInUserId) &&
                                    msg.getReceiver().getId().equals(userId)) ||
                                    (msg.getSender().getId().equals(userId) &&
                                            msg.getReceiver().getId().equals(loggedInUserId)))
                    .toList();

            if (conversation.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No conversation found with this user.");
            }

            return ResponseEntity.ok(conversation);
        }catch (NullPointerException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: Missing user details");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @Secured({"ROLE_BASIC","ROLE_ADMIN"})
    @GetMapping("/received") //correct
    public ResponseEntity<?> getReceivedMessages(@AuthenticationPrincipal UserDetailsImpl auth) {


        try {
            Long loggedInUserId = auth.getId();

            List<Messages> receivedMessages = mService.getMessages().stream()
                    .filter(msg -> msg.getReceiver().getId().equals(loggedInUserId))
                    .toList();

            if (receivedMessages.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No received messages found.");
            }

            return ResponseEntity.ok(receivedMessages);

        }catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid request: Missing user details.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }



    }

}
