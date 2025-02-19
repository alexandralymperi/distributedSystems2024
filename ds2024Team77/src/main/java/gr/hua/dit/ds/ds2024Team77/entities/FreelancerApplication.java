package gr.hua.dit.ds.ds2024Team77.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

//The FreelancerApplication entity represents a request from a user to become a freelancer.
//It includes information such as the user who made the request, a description, and the status of the request.
@Entity
public class FreelancerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonIgnore
    private Long Id;

    //The user applying to become a freelancer.Many-to-One relationship, as a user may have submitted one or more applications.
    //Ignored during JSON serialization to avoid circular dependencies.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User apFreelancer;

    @Column
    private String description;

    @Column
    @NotBlank
    @JsonIgnore
    private String status;

    //Constructor
    public FreelancerApplication(User apFreelancer, String description, String status) {
        this.apFreelancer = apFreelancer;
        this.description = description;
        this.status = status;
    }
    //Default constructor (required by JPA).
    public FreelancerApplication() {

    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public User getApFreelancer() {
        return apFreelancer;
    }

    public void setApFreelancer(User apFreelancer) {
        this.apFreelancer = apFreelancer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //toString method
    @Override
    public String toString() {
        return "FreelancerApplication{" +
                "Id=" + Id +
                ", apFreelancer=" + apFreelancer +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
