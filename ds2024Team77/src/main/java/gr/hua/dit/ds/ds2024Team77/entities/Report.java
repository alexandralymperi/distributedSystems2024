package gr.hua.dit.ds.ds2024Team77.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Date;

//The Report entity represents a report (complaint) made by a user.
@Entity
public class Report {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonIgnore
    private Long Id;

    //The user who submitted the report. Many-to-One relationship, as a user can have many references.
    //Ignored during JSON serialization.
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "reporter_id")
    @JsonIgnore
    private User reporter;

    @Column
    @NotBlank
    @Size(max = 20)
    private String title;

    @Column
    @NotBlank
    private String complaint;

    @Column
    @NotNull
    private Date date;

    @Column
    @NotBlank
    @JsonIgnore
    private String status;

    //Constructors
    public Report(String title, String complaint, Date date) {
        this.title = title;
        this.complaint = complaint;
        this.date = date;
    }
    //Default constructor (required by JPA).
    public Report() {

    }

    //Setters & Getters
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public User getReporter() {
        return reporter;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
