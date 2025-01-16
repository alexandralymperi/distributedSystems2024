package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectApplicationsService;
import gr.hua.dit.ds.ds2024Team77.service.ProjectService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/projects")
public class ProjectsController {

    ArrayList<Project> projects = new ArrayList<Project>();
    ArrayList<ProjectApplications> applications = new ArrayList<ProjectApplications>();
    private ProjectRepository pRepository;
    private ProjectApplicationsRepository pARepository;
    private ProjectService pService;
    private ProjectApplicationsService pAService;

    public ProjectsController(ProjectRepository pRepository, ProjectService pService) {
        this.pRepository = pRepository;
        this.pService = pService;
    }

    public List<ProjectApplications> getApplications() {
        return applications;
    }

    @GetMapping("/assign")
    public void assignFreelancerToProject(@PathVariable Long id, Model model, User freelancer){

        Project project = pRepository.findById(id).get();
        project.setFreelancer(freelancer);
        pRepository.save(project);
    }


}
