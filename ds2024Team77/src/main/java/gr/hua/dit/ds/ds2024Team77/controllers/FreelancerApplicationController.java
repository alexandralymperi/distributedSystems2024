package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.repository.FreelancerApplicationRepository;
import gr.hua.dit.ds.ds2024Team77.service.FreelancerApplicationService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/FreelancerApplication")
public class FreelancerApplicationController {

    private FreelancerApplicationService freelancerApplicationService;
    private UserService userService;

    public FreelancerApplicationController(FreelancerApplicationService freelancerApplicationService, UserService userService) {
        this.freelancerApplicationService = freelancerApplicationService;
        this.userService = userService;
    }

    @GetMapping("")
    public List<FreelancerApplication> getProjectApplications(){
        return freelancerApplicationService.getFreelancerApplications();
    }

    @GetMapping("/{freelancerapplicationId}")
    public ResponseEntity<FreelancerApplication> getProjectApplication(@PathVariable Long freelancerapplicationId){
        Optional<FreelancerApplication> freelancerapplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
        return freelancerapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("/{freelancerapplicationId}")
    public void createFreelancerApplication(@RequestBody FreelancerApplication freelancerapplication,
                                          @AuthenticationPrincipal UserDetailsImpl auth,
                                          @PathVariable Long freelancerapplicationId){
        freelancerapplication.setApFreelancer(userService.getUser(auth.getId()).get());
        freelancerApplicationService.saveFreelancerApplication(freelancerapplication);
    }

    @DeleteMapping("/{freelancerapplicationId}")
    public ResponseEntity<String> deleteFreelancerApplication(@PathVariable Long freelancerapplicationId){

        boolean result = this.freelancerApplicationService.deleteApplication(freelancerapplicationId);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Application deleted successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application deletion unsuccessful.");
        }

    }

    @PutMapping("/{freelancerapplicationId}/accept")
    public ResponseEntity<String> acceptFreelancerApplication(@PathVariable Long freelancerapplicationId) {

        Optional<FreelancerApplication> optionalFreelancerApplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
        if (optionalFreelancerApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
        }

        FreelancerApplication application = optionalFreelancerApplication.get();

        application.setStatus("ACCEPTED");

        freelancerApplicationService.saveFreelancerApplication(application);

        return ResponseEntity.status(HttpStatus.OK).body("Freelancer application accepted.");
    }

    @PutMapping("/{freelancerapplicationId}/reject")
    public ResponseEntity<String> rejectFreelancerApplication(@PathVariable Long freelancerapplicationId) {

        Optional<FreelancerApplication> optionalFreelancerApplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
        if (optionalFreelancerApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
        }

        FreelancerApplication application = optionalFreelancerApplication.get();

        application.setStatus("REJECTED");

        freelancerApplicationService.saveFreelancerApplication(application);

        return ResponseEntity.status(HttpStatus.OK).body("Freelancer application rejected.");
    }

}
