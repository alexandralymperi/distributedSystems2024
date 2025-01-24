package gr.hua.dit.ds.ds2024Team77.controllers;


import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import gr.hua.dit.ds.ds2024Team77.entities.Messages;
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

    @GetMapping("")
    public List<Messages> getMessages(){
        return mService.getMessages();
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<Messages> getMessage(@PathVariable Long messageId){
        Optional<Messages> message = mService.getMessage(messageId);
        return message.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public void createMessage(@RequestBody Messages message, @AuthenticationPrincipal UserDetailsImpl auth){
        message.setSender(userService.getUser(auth.getId()).get());
        mService.saveMessages(message);
    }

    @DeleteMapping("/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long messageId){

        boolean result = this.mService.deleteMessageById(messageId);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Message deleted successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message deletion unsuccessful.");
        }

    }

    @GetMapping("/{messageId}/edit")
    public ResponseEntity<String> editMessageContents(@PathVariable Long messageId, Integer senderId, String newContent, Model model) {
        Messages message = mRepository.findById(messageId).get();

        message.setContents(newContent);
        mRepository.save(message);

        return ResponseEntity.status(senderId).body("Edited");
    }

    @GetMapping("/{messageId}/change")
    public String changeMessageStatusToRead(@PathVariable Long messageId, Model model) {
        Messages message = mRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        message.setStatus("READ");
        mRepository.save(message);
        return "messages";
    }
}
