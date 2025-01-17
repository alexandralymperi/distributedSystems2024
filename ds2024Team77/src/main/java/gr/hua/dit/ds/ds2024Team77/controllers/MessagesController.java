package gr.hua.dit.ds.ds2024Team77.controllers;


import org.springframework.stereotype.Controller;
import gr.hua.dit.ds.ds2024Team77.entities.Messages;
import gr.hua.dit.ds.ds2024Team77.repository.MessagesRepository;
import gr.hua.dit.ds.ds2024Team77.service.MessagesService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessagesController {


    private MessagesRepository mRepository;
    private MessagesService mService;

    public MessagesController(MessagesRepository mRepository, MessagesService mService) {
        this.mRepository = mRepository;
        this.mService = mService;
    }

    @GetMapping("")
    public List<Messages> getMessages(){
        return mService.getMessages();
    } //Αυτό δεν ξέρω αν χρειάζετε.

    @GetMapping("/new")
    public String newMessages(Model model){
        Messages message = new Messages();
        model.addAttribute("message",message ); // Στο attributeName δεν ξέρω αν θέλει report ή Report
        return "messages";
    }

    @PostMapping("/new")
    public void saveMessages(@PathVariable("/Messages") Messages messages, Model model){
        mService.saveMessages(messages);
        model.addAttribute("messages", mService.getMessages());
        model.addAttribute("successMessage", "Message added successfully!");
        mRepository.save(messages);
    }

    @GetMapping("/edit")
    public String editMessageContents(@PathVariable Integer messageId, Integer senderId, String newContent, Model model) {
        Messages message = mRepository.findById(messageId).get();

        message.setContents(newContent);
        mRepository.save(message);
        return "messages";
    }
    @GetMapping("/change")
    public String changeMessageStatusToRead(@PathVariable Integer messageId, Model model) {
        Messages message = mRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        message.setStatus("READ"); // Υποθέτουμε ότι το status είναι String και η τιμή "READ" σημαίνει διαβασμένο.
        mRepository.save(message);
        return "messages";
    }
}
