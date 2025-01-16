package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Review;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;

    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    public Optional<Review> getReview(Long id){return reviewRepository.findById(id);}

    @Transactional
    public void saveReview(Review review){
        reviewRepository.save(review);
    }

    @Transactional
    public List<Review> getReviews(){ return reviewRepository.findAll(); }

    @Transactional
    public Optional<Review> getReviewsByReviewee(Long revieweeId){return reviewRepository.getByReviewee_Id(revieweeId);}

    @Transactional
    public boolean deleteReviewById(final Long reviewId){
        final Optional<Review> userOptional = this.reviewRepository.findById(reviewId);
        if(userOptional.isEmpty()){
            return false;
        }
        this.reviewRepository.deleteById(reviewId);
        return true;
    }

}
