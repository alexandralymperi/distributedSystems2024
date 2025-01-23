package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.entities.Review;
import gr.hua.dit.ds.ds2024Team77.repository.ReviewRepository;
import gr.hua.dit.ds.ds2024Team77.service.ReviewService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewRepository rRepository;
    private ReviewService rService;
    private UserService userService;

    public ReviewController(ReviewRepository rRepository, ReviewService rService, UserService userService) {
        this.rRepository = rRepository;
        this.rService = rService;
        this.userService = userService;
    }

    public List<Review> getReviews(){
        return rService.getReviews();
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<Review> getReview(@PathVariable Long reviewId){
        Optional<Review> review = rService.getReview(reviewId);
        return review.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public void createReview(@RequestBody Review review, @AuthenticationPrincipal UserDetailsImpl auth){
        review.setReviewer(userService.getUser(auth.getId()).get());
        rService.saveReview(review);
    }

//    @PostMapping("/new")
//    public String saveReview(@ModelAttribute("/Review") Review review, Model model){
//        rService.saveReview(review);
//        model.addAttribute("Review", rService.getReview(review.getId()));
//        model.addAttribute("successMessage", "Review added successfully!");
//        rRepository.save(review); //Αυτή την γραμμή δεν ξέρω αν την χρειάζετε.
//        return "reviews";
//    }

    @GetMapping("/show-reviews-by")
    public ResponseEntity<Review> showReviewsByReviewee(@PathVariable Review review, Long revieweeId){
        Optional<Review> reviewee = this.rService.getReview(revieweeId);
        return reviewee.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/show")
    public List<Review> showReviews(){
        return this.rService.getReviews();
    }

}
