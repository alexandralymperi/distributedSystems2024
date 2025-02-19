package gr.hua.dit.ds.ds2024Team77.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

//The Messages entity represents a message exchanged between two users.
//It contains information such as the sender, recipient, content, date, and status of the message.
@Entity
public class Messages {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer Id;

    //The user who sent the message.Many-to-One relationship, as one user may have sent many messages.
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "sender_id")
    //@JsonIgnore
    private User sender;

    //The user who received the message.Many-to-One relationship, as a user may have received many messages.
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column
    @NotBlank
    private String contents;

    @Column
    @JsonIgnore
    private String status;

    @Column
    private Date date;

    //Constructors
    public Messages(User sender, User receiver, String contents, String status, Date date) {
        this.sender = sender;
        this.receiver = receiver;
        this.contents = contents;
        this.status = "DELIVERED";
        this.date = date;
    }
    //Default constructor (required by JPA).
    public Messages() {

    }

    //Getters & Setters

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return  contents + '\'' ;
    }
}
