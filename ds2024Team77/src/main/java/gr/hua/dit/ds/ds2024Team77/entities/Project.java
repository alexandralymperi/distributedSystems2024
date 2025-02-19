package gr.hua.dit.ds.ds2024Team77.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

//The Project entity represents a project that can be assigned to a freelancer.
//It includes information such as title, description, pay, status, and user requests.
@Entity
public class Project {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long Id;

    @Column
    @NotBlank
    private String title;

    @Column
    @NotBlank
    private String description;

    @Column
    @NotNull
    @Min(value = 1)
    private float pay;

    @Column
    @NotBlank
    private String status;

    //Mapping
    //The user who created the project (client). Many-to-One relationship, as one client can create many projects
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "customer_id")
    private User customer;

    //The freelancer who took on the project. Many-to-One relationship, as a freelancer may have taken on many projects.
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "freelancer_id")
    private User freelancer;

    //The requests that have been made for the project by freelancers. One-to-Many relationship, as a project can have many requests.
    //Ignored during JSON serialization to avoid circular dependencies.
    @JsonIgnore
    @OneToMany(mappedBy = "project", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.PERSIST})
    private List<ProjectApplications> applications;


    //Constructors
    public Project(String title, String description, float pay, String status) {
        this.title = title;
        this.description = description;
        this.pay = pay;
        this.status = status;
    }
    //Default constructor (required by JPA).
    public Project() {

    }

    //Setters & Getters
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPay() {
        return pay;
    }

    public void setPay(float pay) {
        this.pay = pay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getCustomer() {
        return customer;
    }

    public void setCustomer(User customer) {
        this.customer = customer;
    }

    public User getFreelancer() {
        return freelancer;
    }

    public void setFreelancer(User freelancer) {
        this.freelancer = freelancer;
    }

    public List<ProjectApplications> getApplications() {
        return applications;
    }

    public void setApplications(List<ProjectApplications> applications) {
        this.applications = applications;
    }
}
