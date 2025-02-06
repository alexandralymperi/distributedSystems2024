package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectApplicationsService;
import gr.hua.dit.ds.ds2024Team77.service.ProjectService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectsController {
    private ProjectService pService;
    private UserService userService;

    public ProjectsController(ProjectService pService, UserService userService) {
        this.pService = pService;
        this.userService = userService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("") //CORRECT
    public ResponseEntity<?> getProjects(){

        try{
            List<Project> projects = pService.getProjects();

            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found.");
            }

            return ResponseEntity.ok(projects);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PostMapping("")//CORRECT
    public ResponseEntity<String> createProject(@RequestBody Project project,
                                                @AuthenticationPrincipal UserDetailsImpl auth){

        try {
            project.setStatus("PENDING_APPROVAL");
            project.setCustomer(userService.getUser(auth.getId()).get());
            pService.saveProject(project);

            return ResponseEntity.status(HttpStatus.CREATED).body("Project created successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @GetMapping("/{projectId}") //CORRECT
    public ResponseEntity<Project> getProject(@PathVariable Long projectId){
        Optional<Project> project = pService.getProject(projectId);
        return project.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @GetMapping("/active") //CORRECT
    public ResponseEntity<?> getActiveProjects() {

        try {
            List<Project> activeProjects = pService.getActiveProjects();

            if (activeProjects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No active projects found.");
            }

            return ResponseEntity.ok(activeProjects);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/customer") //CORRECT
    public ResponseEntity<?> getProjectsByCustomer(@AuthenticationPrincipal UserDetailsImpl auth) {

        try {
            List<Project> projects = pService.getProjectsBycCustomer(auth.getId());

            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found for the customer.");
            }

            return ResponseEntity.ok(projects);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @GetMapping("/freelancer") //CORRECT
    public ResponseEntity<?> getProjectsByFreelancer(@AuthenticationPrincipal UserDetailsImpl auth) {

        try {
            List<Project> projects = pService.getProjectsByFreelancer_Id(auth.getId());

            if (projects.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No projects found for the freelancer.");
            }

            return ResponseEntity.ok(projects);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{projectId}/approve") //CORRECT
    public ResponseEntity<String> approveProject(@PathVariable Long projectId) {

        try{
            boolean result = pService.approveProject(projectId);
            if (result) {
                return ResponseEntity.status(HttpStatus.OK).body("Project approved successfully.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error approving project: " + e.getMessage());
        }

    }

}
