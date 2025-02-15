package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Transactional
    public Optional<Project> getProject(Long id){return projectRepository.findById(id);}

    @Transactional
    public Project saveProject(Project project){
        return projectRepository.save(project);
    }

    @Transactional
    public List<Project> getProjects(){ return projectRepository.findAll(); }

    @Transactional
    public void assignFreelancerToProject(Long projectId, User freelancer){
        Project project = projectRepository.findById(projectId).get();
        project.setFreelancer(freelancer);
        projectRepository.save(project);
    }

    @Transactional
    public List<Project> getByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    @Transactional
    public boolean deleteProjectById(final Long projectId){

        Optional<Project> userOptional = this.projectRepository.findById(projectId);

        if(userOptional.isEmpty()){
            return false;
        }else{
            this.projectRepository.deleteById(projectId);
            return true;
        }

    }

    @Transactional
    public List<Project> getProjectsByFreelancer_Id(Long freelancerId) {
        return projectRepository.findByFreelancer_Id(freelancerId);
    }

    @Transactional
    public List<Project> getProjectsBycCustomer(Long customerId) {
        return projectRepository.findByCustomer_Id(customerId);
    }

    @Transactional
    public List<Project> getActiveProjects() {
        return projectRepository.findByStatus("ACTIVE");
    }


    public boolean approveProject(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        if (optionalProject.isPresent()) {
            Project project = optionalProject.get();
            project.setStatus("ACTIVE");
            projectRepository.save(project);
            return true;
        }
        return false;
    }


}
