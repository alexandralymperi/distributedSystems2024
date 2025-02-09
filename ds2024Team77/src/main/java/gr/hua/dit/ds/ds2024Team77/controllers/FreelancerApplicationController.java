package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.entities.Role;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.FreelancerApplicationRepository;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;
import gr.hua.dit.ds.ds2024Team77.service.FreelancerApplicationService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.web.servlet.function.ServerResponse.status;

@RestController
@RequestMapping("/FreelancerApplication")
public class FreelancerApplicationController {

    private FreelancerApplicationService freelancerApplicationService;
    private UserService userService;

    private RoleRepository roleRepository;

    private UserRepository userRepository;

    public FreelancerApplicationController(FreelancerApplicationService freelancerApplicationService,
                                           UserService userService, RoleRepository roleRepository,
                                           UserRepository userRepository) {
        this.freelancerApplicationService = freelancerApplicationService;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("") //CORRECT
    public List<FreelancerApplication> getFreelancerApplications() {
        //return freelancerApplicationService.getFreelancerApplications();

        List<FreelancerApplication> allApplications = freelancerApplicationService.getFreelancerApplications();

        List<FreelancerApplication> pendingApplications = new ArrayList<>();
        for (FreelancerApplication app : allApplications) {
            if (app.getStatus().equals("PENDING")) {
                pendingApplications.add(app);

            }
        }

        return pendingApplications;
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @GetMapping("/{freelancerapplicationId}") //CORRECT
    public ResponseEntity<?> getFreelancerApplication(@PathVariable Long freelancerapplicationId,
                                                                          @AuthenticationPrincipal UserDetailsImpl auth){
        Optional<FreelancerApplication> freelancerapplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);

        FreelancerApplication application = freelancerapplication.get();

        if (!application.getApFreelancer().getId().equals(auth.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to evaluate this application.");
        }

        return freelancerapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Secured("ROLE_BASIC")
    @PostMapping("/create") //CORRECT
    public ResponseEntity<String> createFreelancerApplication(@RequestBody FreelancerApplication freelancerapplication,
                                          @AuthenticationPrincipal UserDetailsImpl auth){
        try{
            freelancerapplication.setApFreelancer(userService.getUser(auth.getId()).get());
            freelancerapplication.setStatus("PENDING");
            freelancerApplicationService.saveFreelancerApplication(freelancerapplication);
            return ResponseEntity.status(HttpStatus.CREATED).body("Freelancer application created successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating freelancer application: " + e.getMessage());
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"}) //CORRECT
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

    @Secured("ROLE_ADMIN") //CORRECT
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

            User freelancer = application.getApFreelancer();
            Optional<Role> roleOptional = roleRepository.findByName("ROLE_FREELANCER");
            System.out.println(roleOptional);

            userService.saveUser(freelancer, true);

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
