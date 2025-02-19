package gr.hua.dit.ds.ds2024Team77.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import org.springframework.stereotype.Repository;
import java.util.List;

//Repository interface for accessing project application data (ProjectApplication).
//It uses Spring Data JPA and extends JpaRepository, allowing CRUD operations and custom search methods.
@Repository
public interface ProjectApplicationsRepository extends JpaRepository<ProjectApplications, Long> {

    //Retrieves all requests related to a specific project.
    //@param project The ID of the project for which we want the requests.
    //@return List of requests corresponding to this project.
    List<ProjectApplications> findApplicationsByProject_Id(Long project);
}
