package gr.hua.dit.ds.ds2024Team77.repository;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreelancerApplicationRepository extends
        JpaRepository<FreelancerApplication, Long> {


}
