package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.repository.FreelancerApplicationRepository;
import gr.hua.dit.ds.ds2024Team77.service.FreelancerApplicationService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@RequestMapping("/FreelancerApplication")
public class FreelancerApplicationController {

    private FreelancerApplicationService freelancerApplicationService;
    private UserService userService;

    public FreelancerApplicationController(FreelancerApplicationService freelancerApplicationService, UserService userService) {
        this.freelancerApplicationService = freelancerApplicationService;
        this.userService = userService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("")
    public List<FreelancerApplication> getFreelancerApplications(){
        return freelancerApplicationService.getFreelancerApplications();
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @GetMapping("/{freelancerapplicationId}")
    public ResponseEntity<?> getFreelancerApplication(@PathVariable Long freelancerapplicationId,
                                                                          @AuthenticationPrincipal UserDetailsImpl auth){
        Optional<FreelancerApplication> freelancerapplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);

        FreelancerApplication application = freelancerapplication.get();

        if (!application.getApFreelancer().getId().equals(auth.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to evaluate this application.");
        }

        return freelancerapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @PostMapping("/create")
    public ResponseEntity<String> createFreelancerApplication(@RequestBody FreelancerApplication freelancerapplication,
                                          @AuthenticationPrincipal UserDetailsImpl auth){
        try{
            freelancerapplication.setApFreelancer(userService.getUser(auth.getId()).get());
            freelancerApplicationService.saveFreelancerApplication(freelancerapplication);
            return ResponseEntity.status(HttpStatus.CREATED).body("Freelancer application created successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating freelancer application: " + e.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @DeleteMapping("/{freelancerapplicationId}")
    public ResponseEntity<String> deleteFreelancerApplication(@PathVariable Long freelancerapplicationId,
                                                              @AuthenticationPrincipal UserDetailsImpl auth){
        try{
            Optional<FreelancerApplication> optionalApplication =
                    freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
            if (optionalApplication.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application with ID " + freelancerapplicationId + " not found.");
            }

            FreelancerApplication application = optionalApplication.get();

            if (application.getApFreelancer().getId().equals(auth.getId())) {
                boolean result = this.freelancerApplicationService.deleteApplication(freelancerapplicationId);
                if (result) {
                    return ResponseEntity.status(HttpStatus.OK).body("Application with ID " + freelancerapplicationId + " deleted successfully.");
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application with ID " + freelancerapplicationId + " not found.");
                }
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to delete this application.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting freelancer application: " + e.getMessage());
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{freelancerapplicationId}/accept")
    public ResponseEntity<String> acceptFreelancerApplication(@PathVariable Long freelancerapplicationId) {

        try{
            Optional<FreelancerApplication> optionalFreelancerApplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
            if (optionalFreelancerApplication.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
            }

            FreelancerApplication application = optionalFreelancerApplication.get();

            application.setStatus("ACCEPTED");

            freelancerApplicationService.saveFreelancerApplication(application);

            return ResponseEntity.status(HttpStatus.OK).body("Freelancer application accepted.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error accepting freelancer application: " + e.getMessage());
        }

    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{freelancerapplicationId}/reject")
    public ResponseEntity<String> rejectFreelancerApplication(@PathVariable Long freelancerapplicationId) {

        try{
            Optional<FreelancerApplication> optionalFreelancerApplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
            if (optionalFreelancerApplication.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
            }

            FreelancerApplication application = optionalFreelancerApplication.get();

            application.setStatus("REJECTED");

            freelancerApplicationService.saveFreelancerApplication(application);

            return ResponseEntity.status(HttpStatus.OK).body("Freelancer application rejected.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error rejecting freelancer application: " + e.getMessage());
        }

    }

}
