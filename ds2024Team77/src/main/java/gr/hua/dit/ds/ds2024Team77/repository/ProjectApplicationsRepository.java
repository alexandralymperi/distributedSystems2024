package gr.hua.dit.ds.ds2024Team77.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProjectApplicationsRepository extends JpaRepository<ProjectApplications, Long> {
    List<ProjectApplications> findApplicationsByProject_Id(Long project);
}
