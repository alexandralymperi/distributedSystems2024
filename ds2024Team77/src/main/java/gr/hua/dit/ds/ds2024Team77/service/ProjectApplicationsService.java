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

@Service
public class ProjectApplicationsService {
    private ProjectApplicationsRepository projectApplicationsRepository;
    private ProjectRepository projectRepository;
    private ProjectService projectService;

    public ProjectApplicationsService(ProjectApplicationsRepository projectApplicationsRepository,
                                      ProjectRepository projectRepository, ProjectService projectService) {
        this.projectApplicationsRepository = projectApplicationsRepository;
        this.projectRepository = projectRepository;
        this.projectService = projectService;
    }

    @Transactional
    public Optional<ProjectApplications> getProjectApplication(Long id) {
        return (projectApplicationsRepository.findById(id));
    }

    @Transactional
    public void saveProjectApplication(ProjectApplications projectApplications) {
        projectApplicationsRepository.save(projectApplications);
    }

    @Transactional
    public List<ProjectApplications> getProjectApplications() {
        return projectApplicationsRepository.findAll();
    }

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

    @Transactional
    public List<ProjectApplications> getApplicationsByProject(Long projectId) {
        return projectApplicationsRepository.findApplicationsByProject_Id(projectId);
    }

    @Transactional
    public List<ProjectApplications> getProjectApplicationsByStatus(Long projectId, String status) {
        List<ProjectApplications> applications = getApplicationsByProject(projectId);

        return applications.stream()
                .filter(application -> application.getStatus().equalsIgnoreCase(status))
                .toList();
    }

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

    @Transactional
    public void rejectApplication(Long applicationId) {

        ProjectApplications application = projectApplicationsRepository.findById(applicationId).get();
        application.setStatus("REJECTED");
        projectApplicationsRepository.save(application);
    }
}
