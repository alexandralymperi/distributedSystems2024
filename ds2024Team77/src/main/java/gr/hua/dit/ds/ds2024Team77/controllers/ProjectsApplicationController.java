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

    private ProjectApplicationsRepository paRepository;
    private ProjectApplicationsService paService;
    private UserService userService;

    private ProjectService projectService;

    public ProjectsApplicationController(ProjectApplicationsRepository paRepository, ProjectApplicationsService paService,
                                         UserService userService, ProjectService projectService) {
        this.paRepository = paRepository;
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
    public void createProjectApplications(@RequestBody ProjectApplications projectapplication, @AuthenticationPrincipal UserDetailsImpl auth,
                                          @PathVariable Long projectId){
        projectapplication.setApplicant(userService.getUser(auth.getId()).get());
        projectapplication.setProject(projectService.getProject(projectId).get());
        paService.saveProjectApplication(projectapplication);
    }

    @GetMapping("/delete")
    public void deleteApplication(Long applicationId) {
        ProjectApplications application = paRepository.findById(applicationId).get();
        paRepository.delete(application);
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

    @GetMapping("/Date") ///????
    public Date getApplicationDate(Date applicationDate) {
        return applicationDate;
    }
}
