package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Project management service.
//It provides functions for saving, retrieving, updating and deleting projects.
@Service
public class ProjectService {

    private ProjectRepository projectRepository;

    //Constructor to initialize the ProjectService with the ProjectRepository.
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    //Searches the database for a project by ID.
    @Transactional
    public Optional<Project> getProject(Long id){return projectRepository.findById(id);}

    @Transactional
    public Project saveProject(Project project){
        return projectRepository.save(project);
    }

    @Transactional
    public List<Project> getProjects(){ return projectRepository.findAll(); }

    //Assigning a freelancer to a project.
    @Transactional
    public void assignFreelancerToProject(Long projectId, User freelancer){
        Project project = projectRepository.findById(projectId).get();
        project.setFreelancer(freelancer);
        projectRepository.save(project);
    }

    //Returns projects that have a specific status.
    @Transactional
    public List<Project> getByStatus(String status) {
        return projectRepository.findByStatus(status);
    }

    //Deletes a project from the database with the given ID.
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

    //returns projects assigned to a specific freelancer (based on their ID).
    @Transactional
    public List<Project> getProjectsByFreelancer_Id(Long freelancerId) {
        return projectRepository.findByFreelancer_Id(freelancerId);
    }

    //Returns the projects belonging to a specific client (based on their ID).
    @Transactional
    public List<Project> getProjectsBycCustomer(Long customerId) {
        return projectRepository.findByCustomer_Id(customerId);
    }

    //Returns the projects that are active.
    @Transactional
    public List<Project> getActiveProjects() {
        return projectRepository.findByStatus("ACTIVE");
    }

    //Activates a project, setting its status to "ACTIVE".
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
