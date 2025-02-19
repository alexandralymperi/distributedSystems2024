package gr.hua.dit.ds.ds2024Team77.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import gr.hua.dit.ds.ds2024Team77.entities.Report;
import org.springframework.stereotype.Repository;
import java.util.List;

//The ReportRepository is an interface that provides methods for accessing and managing reports in the database.
//It inherits from JpaRepository to take advantage of out-of-the-box CRUD functions
@Repository
public interface ReportRepository extends JpaRepository<Report,Long> {

    //Searches all reports based on their status.
    //@param status The status of the report (eg, "PENDING", "REVIEWED").
    //@return A list of references that have the specified status.
    List<Report> findByStatus(String status);
}
