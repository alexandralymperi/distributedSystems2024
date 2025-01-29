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
    public List<Messages> getMessages(){
        return mService.getMessages();
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @GetMapping("/{messageId}")
    public ResponseEntity<Messages> getMessage(@PathVariable Long messageId){
        Optional<Messages> message = mService.getMessage(messageId);
        return message.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_BASIC"})
    @PostMapping("/send/{recipientId}")
    public ResponseEntity<String> sendMessage(
            @PathVariable Long recipientId,
            @RequestBody Messages message,
            @AuthenticationPrincipal UserDetailsImpl auth) {

        Long senderId = auth.getId();

        Optional<User> recipientOpt = userService.getUser(recipientId);
        if (recipientOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Recipient not found.");
        }

        message.setSender(userService.getUser(senderId).get());
        message.setReceiver(recipientOpt.get());

        mService.saveMessages(message);

        return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully.");
    }

//    @Secured({"ROLE_BASIC"})
//    @DeleteMapping("/{messageId}")
//    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId,
//                                                @AuthenticationPrincipal UserDetailsImpl auth){
//
//        Optional<Messages> messageOptional = mService.getMessage(messageId);
//
//        boolean result = this.mService.deleteMessageById(messageId);
//
//        Messages message = messageOptional.get();
//
//        if (!message.getSender().getId().equals(auth.getId())) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this project.");
//        }
//        if(result){
//            return ResponseEntity.status(HttpStatus.OK).body("Message deleted successfully.");
//        }else{
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message deletion unsuccessful.");
//        }
//
//    }

//    @GetMapping("/{messageId}/edit")
//    public ResponseEntity<String> editMessageContents(@PathVariable Long messageId, Integer senderId, String newContent, Model model) {
//        Messages message = mRepository.findById(messageId).get();
//
//        message.setContents(newContent);
//        mRepository.save(message);
//
//        return ResponseEntity.status(senderId).body("Edited");
//    }

//    @Secured({"ROLE_BASIC","ROLE_ADMIN"})
//    @GetMapping("/{messageId}/change")
//    public String changeMessageStatusToRead(@PathVariable Long messageId, Model model) {
//        Messages message = mRepository.findById(messageId)
//                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));
//
//        message.setStatus("READ");
//        mRepository.save(message);
//        return "messages";
//    }

    @Secured({"ROLE_BASIC","ROLE_ADMIN"})
    @GetMapping("/conversation/{userId}")
    public List<Messages> getConversation(
            @PathVariable Long userId,
            @AuthenticationPrincipal UserDetailsImpl auth) {

        Long loggedInUserId = auth.getId();

        List<Messages> allMessages = mService.getMessages();

        List<Messages> conversation = allMessages.stream()
                .filter(msg ->
                        (msg.getSender().getId().equals(loggedInUserId) &&
                                msg.getReceiver().getId().equals(userId)) ||
                                (msg.getSender().getId().equals(userId) &&
                                        msg.getReceiver().getId().equals(loggedInUserId)))
                .toList();

        return conversation;
    }

    @Secured({"ROLE_BASIC","ROLE_ADMIN"})
    @GetMapping("/received")
    public List<Messages> getReceivedMessages(@AuthenticationPrincipal UserDetailsImpl auth) {

        Long loggedInUserId = auth.getId();

        List<Messages> receivedMessages = mService.getMessages().stream()
                .filter(msg -> msg.getReceiver().getId().equals(loggedInUserId))
                .toList();

        return receivedMessages;
    }

}
