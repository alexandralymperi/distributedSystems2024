package gr.hua.dit.ds.ds2024Team77.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.ds.ds2024Team77.entities.Project;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

//The ProjectRepository is an interface that provides methods for accessing and managing projects in the database.
//It inherits from JpaRepository to take advantage of out-of-the-box CRUD functions.
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    //Searches all projects based on their status.
    //@param status The status of the project (eg, "OPEN", "IN_PROGRESS", "COMPLETED").
    //@return A list of projects that have the specified state.
    List<Project> findByStatus(String status);

    //Searches for all projects assigned to a specific freelancer.
    List<Project> findByFreelancer_Id(Long freelancerId);

    //Searches for all projects created by a specific client.
    List<Project> findByCustomer_Id(Long ownerId);

    //Searches for a project based on its title.
    Optional<Project> findByTitle(String title);
}
