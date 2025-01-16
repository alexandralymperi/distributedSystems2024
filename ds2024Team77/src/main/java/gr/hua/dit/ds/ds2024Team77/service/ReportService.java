package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.entities.Review;
import gr.hua.dit.ds.ds2024Team77.repository.ReportRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReportService {

    private ReportRepository reportRepository;

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    public Report getReport(Long id){return reportRepository.findById(id).get();}

    @Transactional
    public void saveReport(Report report){
        reportRepository.save(report);
    }

    @Transactional
    public List<Report> getReports(){ return reportRepository.findAll(); }

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
