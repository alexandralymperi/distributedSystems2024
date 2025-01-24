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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    private ProjectRepository pRepository;
    private ProjectService pService;
    private ProjectApplicationsRepository pARepository;
    private ProjectApplicationsService pAService;
    private UserService userService;

    public ProjectsController(ProjectRepository pRepository, ProjectService pService,
                              ProjectApplicationsRepository pARepository,
                              ProjectApplicationsService pAService, UserService userService) {
        this.pRepository = pRepository;
        this.pService = pService;
        this.pARepository = pARepository;
        this.pAService = pAService;
        this.userService = userService;
    }

    @GetMapping("")
    public List<Project> getProjects(){
        return pService.getProjects();
    }

    @PostMapping("")
    public void createProject(@RequestBody Project project,
                                 @AuthenticationPrincipal UserDetailsImpl auth){

        project.setStatus("PENDING_APPROVAL");
        project.setCustomer(userService.getUser(auth.getId()).get());
        pService.saveProject(project);
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProject(@PathVariable Long projectId){
        Optional<Project> project = pService.getProject(projectId);
        return project.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<String> deleteProject(@PathVariable Long projectId){

        boolean result = this.pService.deleteProjectById(projectId);
        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Project deleted successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project deletion unsuccessful.");
        }

    }

    @GetMapping("/active")
    public List<Project> getActiveProjects() {
        return pService.getActiveProjects();
    }

    @GetMapping("/customer")
    public List<Project> getProjectsByCustomer(@AuthenticationPrincipal UserDetailsImpl auth) {
        return pService.getProjectsBycCustomer(auth.getId());
    }

    @GetMapping("/freelancer")
    public List<Project> getProjectsByFreelancer(@AuthenticationPrincipal UserDetailsImpl auth) {
        return pService.getProjectsByFreelancer_Id(auth.getId());
    }

    @PutMapping("/{projectId}/approve")
    public ResponseEntity<String> approveProject(@PathVariable Long projectId) {
        boolean result = pService.approveProject(projectId);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("Project approved successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Project not found.");
        }
    }

}
