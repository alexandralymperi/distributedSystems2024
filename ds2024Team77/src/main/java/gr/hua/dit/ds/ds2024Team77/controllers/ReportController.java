package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.repository.ReportRepository;
import gr.hua.dit.ds.ds2024Team77.service.ReportService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/report")
public class ReportController {
    private ReportRepository rRepository;
    private ReportService rService;
    private UserService userService;

    public ReportController(ReportRepository rRepository, ReportService rService, UserService userService) {
        this.rRepository = rRepository;
        this.rService = rService;
        this.userService =userService;
    }

    @Secured({"ROLE_ADMIN"})
    @GetMapping("")
    public List<Report> getReports(){
        return rService.getReports();
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @GetMapping("/{reportId}")
    public ResponseEntity<Report> getReport(@PathVariable Long reportId){
        Optional<Report> report = rService.getReport(reportId);
        return report.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

    @Secured({"ROLE_BASIC"})
    @PostMapping("")
    public void createReport(@RequestBody Report report, @AuthenticationPrincipal UserDetailsImpl auth){
        report.setReporter(userService.getUser(auth.getId()).get());
        rService.saveReport(report);
    }

    @Secured({"ROLE_BASIC"})
    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId,
                                               @AuthenticationPrincipal UserDetailsImpl auth){

        Optional<Report> reportOptional = rService.getReport(reportId);

        boolean result = this.rService.deleteReportById(reportId);

        Report report = reportOptional.get();

        if (!report.getReporter().getId().equals(auth.getId())) {
             return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this project.");
        }

        if(result){
            return ResponseEntity.status(HttpStatus.OK).body("Report deleted successfully.");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Report deletion unsuccessful.");
        }

    }

}
