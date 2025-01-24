package gr.hua.dit.ds.ds2024Team77.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.ds.ds2024Team77.entities.Project;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStatus(String status);
    List<Project> findByFreelancer_Id(Long freelancerId);
    List<Project> findByCustomer_Id(Long ownerId);
    Optional<Project> findByTitle(String title);
}
