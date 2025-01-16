package gr.hua.dit.ds.ds2024Team77.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

@Entity
@Table
public class UserProfile {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    @OneToOne(mappedBy = "profile", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    private User user;

    @Column
    private String bio;

    @Column
    private String phoneNumber;

    @Column
    @Size(max = 50)
    private String address;

    //Constructors
    public UserProfile(User user, String bio, String phoneNumber, String address) {
        this.user = user;
        this.bio = bio;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public UserProfile() {

    }

    //Setters & Getters
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
