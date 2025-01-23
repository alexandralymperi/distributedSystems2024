package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.entities.Review;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectApplicationsService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ProjectApplication")
public class ProjectsApplicationController {

    ArrayList<ProjectApplications> paList = new ArrayList<ProjectApplications>();
    private ProjectApplicationsRepository paRepository;
    private ProjectApplicationsService paService;
    private UserService userService;

    public ProjectsApplicationController(ProjectApplicationsRepository paRepository, ProjectApplicationsService paService, UserService userService) {
        this.paRepository = paRepository;
        this.paService = paService;
        this.userService =userService;
    }

    @GetMapping("")
    public List<ProjectApplications> getProjectApplications(){
        return paService.getProjectApplications();
    }

    @GetMapping("/{projectapplicationId}")
    public ResponseEntity<ProjectApplications> getReport(@PathVariable Long projectapplicationId){
        Optional<ProjectApplications> projectapplication = paService.getProjectApplication(projectapplicationId);
        return projectapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping("")
    public void createProjectApplications(@RequestBody ProjectApplications projectapplication, @AuthenticationPrincipal UserDetailsImpl auth){
        projectapplication.setApplicant(userService.getUser(auth.getId()).get());
        paService.saveProjectApplication(projectapplication);
    }

    @GetMapping("/show")
    public List<ProjectApplications> showProjectApplications(){
        return this.paService.getProjectApplications();
    }

    @GetMapping("/shows")
    public ResponseEntity<ProjectApplications> showProjectApplication(@PathVariable Long ProjectApplicationId){
        Optional<ProjectApplications> projectapplication = this.paService.getProjectApplication(ProjectApplicationId);
        return projectapplication.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/delete")
    public void deleteApplication(Long applicationId) {
        ProjectApplications application = paRepository.findById(applicationId).get();
        paRepository.delete(application);
    }

    @GetMapping("/Date") ///????
    public Date getApplicationDate(Date applicationDate) {
        return applicationDate;
    }
}
