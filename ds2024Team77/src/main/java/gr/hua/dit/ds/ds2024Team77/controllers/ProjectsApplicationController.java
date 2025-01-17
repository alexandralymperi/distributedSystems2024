package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectApplicationsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/ProjectApplication")
public class ProjectsApplicationController {

    ArrayList<ProjectApplications> paList = new ArrayList<ProjectApplications>();
    private ProjectApplicationsRepository paRepository;
    private ProjectApplicationsService paService;

    public ProjectsApplicationController(ProjectApplicationsRepository paRepository, ProjectApplicationsService paService) {
        this.paRepository = paRepository;
        this.paService = paService;
    }

    @PostMapping("/getProjectApplication")
    public ProjectApplications getProjectApplication(Integer id){
        return paRepository.findById(id).get();
    }

    @GetMapping("/delete")
    public void deleteApplication(Integer applicationId) {
        ProjectApplications application = paRepository.findById(applicationId).get();
        paRepository.delete(application);
    }

    @GetMapping("/Date") ///????
    public Date getApplicationDate(Date applicationDate) {
        return applicationDate;
    }
}
