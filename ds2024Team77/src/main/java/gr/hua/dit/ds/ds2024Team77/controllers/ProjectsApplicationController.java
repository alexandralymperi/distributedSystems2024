package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectApplicationsService;
import gr.hua.dit.ds.ds2024Team77.service.ProjectService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/ProjectApplication")
public class ProjectsApplicationController {

    private ProjectApplicationsService paService;
    private UserService userService;

    private ProjectService projectService;

    public ProjectsApplicationController(ProjectApplicationsService paService,
                                         UserService userService, ProjectService projectService) {
        this.paService = paService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("")
    public List<ProjectApplications> getProjectApplications(){
        return paService.getProjectApplications();
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @GetMapping("/{projectapplicationId}")
    public ResponseEntity<?> getProjectApplication(@PathVariable Long projectapplicationId,
                                                   @AuthenticationPrincipal UserDetailsImpl auth){
        Optional<ProjectApplications> projectapplication = paService.getProjectApplication(projectapplicationId);

        ProjectApplications application = projectapplication.get();

        if (!application.getApplicant().getId().equals(auth.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to evaluate this application.");
        }

        return projectapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_FREELANCER"})
    @PostMapping("/{projectId}")
    public void createProjectApplications(@RequestBody ProjectApplications projectapplication,
                                          @AuthenticationPrincipal UserDetailsImpl auth,
                                          @PathVariable Long projectId){
        projectapplication.setApplicant(userService.getUser(auth.getId()).get());
        projectapplication.setProject(projectService.getProject(projectId).get());
        paService.saveProjectApplication(projectapplication);
    }

    @Secured({"ROLE_FREELANCER"})
    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteProjectApplication(@PathVariable Long applicationId){

        boolean result = this.paService.deleteApplication(applicationId);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Application deleted successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application deletion unsuccessful.");
        }
    }

    @Secured({"ROLE_USER"})
    @PutMapping("/{applicationId}/accept")
    public ResponseEntity<String> acceptApplication(@PathVariable Long applicationId,
                                                    @AuthenticationPrincipal UserDetailsImpl auth) {

        Optional<ProjectApplications> existingApplication = paService.getProjectApplication(applicationId);

        if (existingApplication == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated application not found.");
        }

        ProjectApplications application = existingApplication.get();

        if (!application.getProject().getCustomer().getId().equals(auth.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to evaluate this application.");
        }

        Optional<ProjectApplications> optionalApplication = paService.getProjectApplication(applicationId);
        if (optionalApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
        }

        var project = application.getProject();

        if (project == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated project not found.");
        }

        project.setFreelancer(application.getApplicant());
        project.setStatus("ONGOING");

        projectService.saveProject(project);

        List<ProjectApplications> allApplications = paService.getApplicationsByProject(project.getId());
        for (ProjectApplications app : allApplications) {
            if (!app.getId().equals(applicationId)) { ///!!!!
                app.setStatus("REJECTED");
                paService.saveProjectApplication(app);
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body("Freelancer application accepted. Project is now ONGOING.");
    }

    @Secured("ROLE_BASIC")
    @GetMapping("/{projectId}/applications")
    public ResponseEntity<List<ProjectApplications>> getApplicationsByProject(@PathVariable Long projectId,
                                                                              @AuthenticationPrincipal UserDetailsImpl auth) {
        try {
            Optional<Project> project = projectService.getProject(projectId);
            if (project.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }

            if (!project.get().getCustomer().getId().equals(auth.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Collections.emptyList());
            }

            List<ProjectApplications> applications = paService.getApplicationsByProject(projectId);
            return ResponseEntity.status(HttpStatus.OK).body(applications);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Secured({"ROLE_BASIC"})
    @PutMapping("/{applicationId}/reject")
    public ResponseEntity<String> rejectApplication(@PathVariable Long applicationId, @AuthenticationPrincipal UserDetailsImpl auth) {
        try {
            Optional<ProjectApplications> existingApplication = paService.getProjectApplication(applicationId);

            if (existingApplication == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Associated application not found.");
            }

            ProjectApplications application = existingApplication.get();

            if (!application.getProject().getCustomer().getId().equals(auth.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to reject this application.");
            }

            paService.rejectApplication(applicationId);
            return ResponseEntity.ok("Application rejected successfully.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }


}
