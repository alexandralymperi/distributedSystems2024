package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import gr.hua.dit.ds.ds2024Team77.repository.MessagesRepository;
import gr.hua.dit.ds.ds2024Team77.entities.Messages;
import java.util.List;
import java.util.Optional;

@Service
public class MessagesService {

    private MessagesRepository messagesRepository;

    public MessagesService(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Transactional
    public List<Messages> getMessages(){
        return messagesRepository.findAll();
    }

    @Transactional
    public Optional<Messages> getMessage(Long message_id){
        return messagesRepository.findById(message_id);
    }

    @Transactional
    public void saveMessages(Messages messages){
        messagesRepository.save(messages);
    }

    @Transactional
    public void changeMessageStatusToRead(Long messageId) {
        Messages message = messagesRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("Message not found with ID: " + messageId));

        message.setStatus("READ");
        messagesRepository.save(message);
    }

    @Transactional
    public void editMessageContents(Long messageId, Integer senderId, String newContent) {
        Messages message = messagesRepository.findById(messageId).get();

        message.setContents(newContent);
        messagesRepository.save(message);
    }

    @Transactional
    public boolean deleteMessageById(final Long messageId){

        Optional<Messages> userOptional = this.messagesRepository.findById(messageId);

        if(userOptional.isEmpty()){
            return false;
        }else{
            this.messagesRepository.deleteById(messageId);
            return true;
        }
    }

}