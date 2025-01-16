package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Review;
import gr.hua.dit.ds.ds2024Team77.repository.ReviewRepository;
import gr.hua.dit.ds.ds2024Team77.service.ReviewService;
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

    public ReviewController(ReviewRepository rRepository, ReviewService rService) {
        this.rRepository = rRepository;
        this.rService = rService;
    }

//    public List<Review> getReview(){
//        return rService.getReview();
//    }

    @GetMapping("/new")
    public String createReviewForm(Model model) {
        Review review = new Review();
        model.addAttribute("review",review );
        return "reviews";
    }

    @PostMapping("/new")
    public String saveReview(@ModelAttribute("/Review") Review review, Model model){
        rService.saveReview(review);
        model.addAttribute("Review", rService.getReview(review.getId()));
        model.addAttribute("successMessage", "Review added successfully!");
        rRepository.save(review); //Αυτή την γραμμή δεν ξέρω αν την χρειάζετε.
        return "reviews";
    }

    @GetMapping("/show-reviews-by")
    public String showReviewsByReviewee(@PathVariable Review review, List<Review> reviewList, Long revieweeId, Model model){
        Optional<Review> review1 = rService.getReview(revieweeId);
        model.addAttribute("reviewList", review1); //παίζει αντί για reviewList να θέλει Review.
        rRepository.getByReviewee_Id(revieweeId);
        return "reviews";
    }

}
