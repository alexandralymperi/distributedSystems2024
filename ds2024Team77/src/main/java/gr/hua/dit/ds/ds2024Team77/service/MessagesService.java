package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import gr.hua.dit.ds.ds2024Team77.repository.MessagesRepository;
import gr.hua.dit.ds.ds2024Team77.entities.Messages;
import java.util.List;
import java.util.Optional;

//Service for managing messages (Messages).
//It provides functions to store, retrieve and download all messages.
@Service
public class MessagesService {

    private MessagesRepository messagesRepository;

    //MessagesService constructor
    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Transactional
    public List<Messages> getMessages(){
        return messagesRepository.findAll();
    }

    //Searches and returns a message based on its ID.
    @Transactional
    public Optional<Messages> getMessage(Long message_id){
        return messagesRepository.findById(message_id);
    }

    //Stores or updates a message in the database.
    @Transactional
    public void saveMessages(Messages messages){
        messagesRepository.save(messages);
    }

}