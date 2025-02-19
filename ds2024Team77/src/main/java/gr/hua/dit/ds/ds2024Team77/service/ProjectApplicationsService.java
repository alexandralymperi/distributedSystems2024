package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectApplicationsRepository;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Service for the management of project applications (Project Applications).
//It provides functions for saving, retrieving, updating, approving, rejecting and deleting requests.
@Service
public class ProjectApplicationsService {
    private ProjectApplicationsRepository projectApplicationsRepository;
    private ProjectRepository projectRepository;
    private ProjectService projectService;

    //Constructor to initialize the ProjectApplicationsService with the corresponding repositories and services.
    public ProjectApplicationsService(ProjectApplicationsRepository projectApplicationsRepository,
                                      ProjectRepository projectRepository, ProjectService projectService) {
        this.projectApplicationsRepository = projectApplicationsRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    //Searches for a project request from the database based on the ID.
    @Transactional
    public Optional<ProjectApplications> getProjectApplication(Long id) {
        return (projectApplicationsRepository.findById(id));
    }

    //Saves or updates a project request in the database.
    @Transactional
    public void saveProjectApplication(ProjectApplications projectApplications) {
        projectApplicationsRepository.save(projectApplications);
    }

    //Returns all project requests from the database.
    @Transactional
    public List<ProjectApplications> getProjectApplications() {
        return projectApplicationsRepository.findAll();
    }

    //Deletes a request from the database based on the ID.
    @Transactional
    public boolean deleteApplication(Long applicationId) {

        Optional<ProjectApplications> userOptional = this.projectApplicationsRepository.findById(applicationId);

        if(userOptional.isEmpty()){
            return false;
        }else{
            this.projectApplicationsRepository.deleteById(applicationId);
            return true;
        }

    }

    //Returns the project requests associated with a specific project (based on projectId).
    @Transactional
    public List<ProjectApplications> getApplicationsByProject(Long projectId) {
        return projectApplicationsRepository.findApplicationsByProject_Id(projectId);
    }

    //Returns project requests that have a specific status.
    //Filters project requests to return only those that match the condition.
    @Transactional
    public List<ProjectApplications> getProjectApplicationsByStatus(Long projectId, String status) {
        List<ProjectApplications> applications = getApplicationsByProject(projectId);

        return applications.stream()
                .filter(application -> application.getStatus().equalsIgnoreCase(status))
                .toList();
    }

    //Approves a project request.
    //When a request is approved, the freelancer is assigned to the project and the project is given the "ONGOING" status.
    @Transactional
    public void approveApplication(Long applicationId) {
        ProjectApplications application = projectApplicationsRepository.findById(applicationId).get();
        application.setStatus("APPROVED");
        projectApplicationsRepository.save(application);

        Project project = application.getProject();
        User freelancer = application.getApplicant();

        projectService.assignFreelancerToProject(project.getId(), freelancer);

        project.setStatus("ONGOING");
        projectRepository.save(project);
    }

    //Rejects a project request.
    //When an application is rejected, the application status is set to "REJECTED".
    @Transactional
    public void rejectApplication(Long applicationId) {

        ProjectApplications application = projectApplicationsRepository.findById(applicationId).get();
        application.setStatus("REJECTED");
        projectApplicationsRepository.save(application);
    }
}
