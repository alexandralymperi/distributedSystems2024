package gr.hua.dit.ds.ds2024Team77.entities;


import jakarta.persistence.*;

@Entity
public class FreelancerApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User apFreelancer;

    @Column
    private String description;

    public FreelancerApplication(User apFreelancer, String description) {
        this.apFreelancer = apFreelancer;
        this.description = description;
    }

    public FreelancerApplication() {

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


}
