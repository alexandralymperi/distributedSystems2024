package gr.hua.dit.ds.ds2024Team77.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class FreelancerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    @JsonIgnore
    private Long Id;

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

    public FreelancerApplication(User apFreelancer, String description, String status) {
        this.apFreelancer = apFreelancer;
        this.description = description;
        this.status = status;
    }

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
