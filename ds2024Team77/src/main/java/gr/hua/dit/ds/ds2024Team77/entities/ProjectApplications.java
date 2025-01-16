package gr.hua.dit.ds.ds2024Team77.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
public class ProjectApplications {


    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer Id;

    //Mappings
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User applicant;

    //More columns
    @Column
    @NotBlank
    private String status;

    @Column
    private Date applicationDate;

    //Constructors
    public ProjectApplications(Project project, User applicant, String status, Date applicationDate) {
        this.project = project;
        this.applicant = applicant;
        this.status = "PENDING";
        this.applicationDate = applicationDate;
    }

    public ProjectApplications() {

    }

    // Getters και Setters
    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        this.Id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public User getApplicant() {
        return applicant;
    }

    public void setApplicant(User applicant) {
        this.applicant = applicant;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(Date applicationDate) {
        this.applicationDate = applicationDate;
    }
}

