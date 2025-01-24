package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectApplicationsService;
import gr.hua.dit.ds.ds2024Team77.service.ProjectService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("")
    public List<ProjectApplications> getProjectApplications(){
        return paService.getProjectApplications();
    }

    @GetMapping("/{projectapplicationId}")
    public ResponseEntity<ProjectApplications> getProjectApplication(@PathVariable Long projectapplicationId){
        Optional<ProjectApplications> projectapplication = paService.getProjectApplication(projectapplicationId);
        return projectapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("/{projectId}")
    public void createProjectApplications(@RequestBody ProjectApplications projectapplication,
                                          @AuthenticationPrincipal UserDetailsImpl auth,
                                          @PathVariable Long projectId){
        projectapplication.setApplicant(userService.getUser(auth.getId()).get());
        projectapplication.setProject(projectService.getProject(projectId).get());
        paService.saveProjectApplication(projectapplication);
    }

    @DeleteMapping("/{applicationId}")
    public ResponseEntity<String> deleteProjectApplication(@PathVariable Long applicationId){

        boolean result = this.paService.deleteApplication(applicationId);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Application deleted successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application deletion unsuccessful.");
        }

    }

    @PutMapping("/{applicationId}/accept")
    public ResponseEntity<String> acceptApplication(@PathVariable Long applicationId) {

        Optional<ProjectApplications> optionalApplication = paService.getProjectApplication(applicationId);
        if (optionalApplication.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Application not found.");
        }

        ProjectApplications application = optionalApplication.get();
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



}
