package gr.hua.dit.ds.ds2024Team77.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

//The ProjectApplications entity represents a user request for a project.
//It contains information about the project, the user who made the request, the date, and the status of the request.
@Entity
public class ProjectApplications {


    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Integer Id;

    //Mappings
    //The project to which the application is submitted. Many-to-One relationship, as a project can have many requests.
    //Ignored during JSON serialization to avoid circular dependencies.
    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    @JsonIgnore
    private Project project;

    //The user who submitted the request. Many-to-One relationship, as a user may have submitted multiple requests.
    //Ignored during JSON serialization.
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User applicant;

    //More columns
    @Column
    @NotBlank
    @JsonIgnore
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
    //Default constructor (required by JPA).
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

