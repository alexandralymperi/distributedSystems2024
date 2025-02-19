package gr.hua.dit.ds.ds2024Team77.repository;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Repository interface for managing freelancer applications (FreelancerApplication).
//It uses Spring Data JPA and extends JpaRepository, allowing basic CRUD (Create, Read, Update, Delete) operations on the database.
@Repository
public interface FreelancerApplicationRepository extends
        JpaRepository<FreelancerApplication, Long> {


}
