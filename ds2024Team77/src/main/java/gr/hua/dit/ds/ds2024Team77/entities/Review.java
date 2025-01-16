package gr.hua.dit.ds.ds2024Team77.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
public class Review {

    //Columns
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long Id;

    @Column
    @NotBlank
    private int value;

    @Column
    private String comments;

    //Mappings
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "reviewer_id")
    private User reviewer;

    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "reviewee_id")
    private User reviewee;

    //Constructors
    public Review(Long id, int value, String comments, User reviewer, User reviewee) {
        Id = id;
        this.value = value;
        this.comments = comments;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
    }

    public Review() {

    }

    //Setters & Getters
    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public User getReviewer() {
        return reviewer;
    }

    public void setReviewer(User reviewer) {
        this.reviewer = reviewer;
    }

    public User getReviewee() {
        return reviewee;
    }

    public void setReviewee(User reviewee) {
        this.reviewee = reviewee;
    }

    @Override
    public String toString() {
        return "GetReview{" +
                ", value ='" + value + '\'' +
                ", comments ='" + comments + '\'' +
                ", reviewer =" + reviewer +
                ", reviewee =" + reviewee +
                '}';
    }

}
