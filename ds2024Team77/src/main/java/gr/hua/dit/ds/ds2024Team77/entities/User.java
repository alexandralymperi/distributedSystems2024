package gr.hua.dit.ds.ds2024Team77.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

//non-admin users
@Entity
@Table(	name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class User {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long Id;

    @NotBlank
    @Size(min=3,max=20)
    private String username;

    @Column
    @NotBlank
    private String name;

    @Column
    @NotBlank
    private String surname;

    @Column(unique = true)
    @NotBlank
    @Size(max=30)
    @Email
    private String email;

    @NotBlank
    //@Size(max = 25)
    private String password;

    //Mappings
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "userRoles",
            joinColumns = @JoinColumn(name="userId"), inverseJoinColumns = @JoinColumn(name = "roleId"))
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "customer", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Project> customerProjects;

    @OneToMany(mappedBy = "freelancer", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Project> freelancerProjects;

    @OneToMany(mappedBy = "sender", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Messages> sentMessages;

    @OneToMany(mappedBy = "receiver", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Messages> receivedMessages;

    @OneToMany(mappedBy = "reporter", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Report> reportsMade;

    @OneToMany(mappedBy = "reviewer", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Review> reviewsLeft;

    @OneToMany(mappedBy = "reviewee", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<Review> reviewsConcerning;

    @OneToMany(mappedBy = "applicant", cascade = {CascadeType.DETACH, CascadeType.MERGE,
            CascadeType.PERSIST, CascadeType.PERSIST})
    private List<ProjectApplications> applications;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_profile_id", referencedColumnName = "Id")
    private UserProfile profile;

    //Constructors
    public User(String username, String name, String surname, String email, String password) {
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }
    public User() {

    }

    //Setters & Getters
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public List<Project> getCustomerProjects() {
        return customerProjects;
    }

    public void setCustomerProjects(List<Project> customerProjects) {
        this.customerProjects = customerProjects;
    }

    public List<Project> getFreelancerProjects() {
        return freelancerProjects;
    }

    public void setFreelancerProjects(List<Project> freelancerProjects) {
        this.freelancerProjects = freelancerProjects;
    }

    public List<Messages> getSentMessages() {
        return sentMessages;
    }

    public void setSentMessages(List<Messages> sentMessages) {
        this.sentMessages = sentMessages;
    }

    public List<Messages> getReceivedMessages() {
        return receivedMessages;
    }

    public void setReceivedMessages(List<Messages> receivedMessages) {
        this.receivedMessages = receivedMessages;
    }

    public List<Report> getReportsMade() {
        return reportsMade;
    }

    public void setReportsMade(List<Report> reportsMade) {
        this.reportsMade = reportsMade;
    }

    public List<Review> getReviewsLeft() {
        return reviewsLeft;
    }

    public void setReviewsLeft(List<Review> reportsLeft) {
        this.reviewsLeft = reviewsLeft;
    }

    public List<Review> getReviewsConcerning() {
        return reviewsConcerning;
    }

    public void setReviewsConcerning(List<Review> reviewsConcerning) {
        this.reviewsConcerning = reviewsConcerning;
    }

    public List<ProjectApplications> getApplications() {
        return applications;
    }

    public void setApplications(List<ProjectApplications> applications) {
        this.applications = applications;
    }

    public UserProfile getProfile() {
        return profile;
    }

    public void setProfile(UserProfile profile) {
        this.profile = profile;
    }

    public void assignCustomerToProject(Project project) {
        customerProjects.add(project);
        project.setCustomer(this);
    }

    public void assignFreelancerToProject(Project project) {
        freelancerProjects.add(project);
        project.setFreelancer(this);
    }

    //toString method
    @Override
    public String toString() {
        return name + '\'' + surname + '\'';
    }
}
