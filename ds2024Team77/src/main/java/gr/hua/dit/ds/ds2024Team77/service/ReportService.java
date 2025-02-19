package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

//Service for the management of reports (Reports).
//It provides functions for saving, retrieving, updating and deleting reports.
@Service
public class ReportService {

    private ReportRepository reportRepository;


    //Constructor to initialize the ReportService with the ReportRepository.
    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    public Optional<Report> getReport(Long id){
        return reportRepository.findById(id);
    }

    @Transactional
    public void saveReport(Report report){
        reportRepository.save(report);
    }

    @Transactional
    public List<Report> getReports(){ return reportRepository.findAll(); }

    //Returns all reports that have a specific status.
    @Transactional
    public List<Report> getReportsByStatus(String status) {
        return reportRepository.findByStatus(status);
    }

    /*@Transactional
    public void updateReportToInProcess(Integer id) {
        Report report = reportRepository.findBy(id);

        report.setStatus("IN_PROCESS");

        reportRepository.save(report);
    }*/

    //Updates the status of the report to "IN_PROCESS".
    //It uses the ID of the report to find and update it.
    @Transactional
    public void updateReportToInProcess(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("There is no report with ID: " + id));

        report.setStatus("IN_PROCESS");

        reportRepository.save(report);
    }

    @Transactional
    public void deleteReport(Long id) {
        reportRepository.deleteById(id);
    }

    //Deletes a reference from the database if it exists.
    //Returns true if the deletion was successful and false if the reference was not found.
    @Transactional
    public boolean deleteReportById(final Long reportId){
        final Optional<Report> userOptional = this.reportRepository.findById(reportId);
        if(userOptional.isEmpty()){
            return false;
        }
        this.reportRepository.deleteById(reportId);
        return true;
    }
}
