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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public ResponseEntity<List<Report>> getReports(){
        try {
            List<Report> reports = rService.getReports();
            return ResponseEntity.ok(reports);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_BASIC"})
    @GetMapping("/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable Long reportId,
                                            @AuthenticationPrincipal UserDetailsImpl auth){

        try {
            Optional<Report> optionalReport = rService.getReport(reportId);

            if (optionalReport.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            Report report = optionalReport.get();

            if (!report.getReporter().getId().equals(auth.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You are not authorized to view this report.");
            }
            return ResponseEntity.ok(report);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }

    }

    @PostMapping("")
    public ResponseEntity<String> createReport(@RequestBody Report report, @AuthenticationPrincipal UserDetailsImpl auth){

        try {
            report.setReporter(userService.getUser(auth.getId()).get());
            report.setStatus("PENDING");
            rService.saveReport(report);

            return ResponseEntity.status(HttpStatus.CREATED).body("Report created successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }

    }

    @Secured({"ROLE_BASIC"})
    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteReport(@PathVariable Long reportId,
                                               @AuthenticationPrincipal UserDetailsImpl auth){

        try {
            Optional<Report> reportOptional = rService.getReport(reportId);

            Report report = reportOptional.get();

            System.out.println(auth.getId());
            System.out.println(report.getReporter().getId());

            if (!auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                    && !report.getReporter().getId().equals(auth.getId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You are not authorized to delete this report.");
            }

            boolean result = this.rService.deleteReportById(reportId);

            if(result){
                return ResponseEntity.status(HttpStatus.OK).body("Report deleted successfully.");
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Report deletion unsuccessful.");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());

        }

    }

    @Secured("ROLE_BASIC")
    @GetMapping("/myreports")
    public ResponseEntity<?> getMyReports(@AuthenticationPrincipal UserDetailsImpl auth){

        try {
            List<Report> allReports = rService.getReports();

            if (allReports.isEmpty()) {
                return ResponseEntity.notFound().build();
            }


            List<Report> myReports = allReports.stream()
                    .filter(report -> report.getReporter().getId().equals(auth.getId()))
                    .collect(Collectors.toList());

            if (myReports.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(myReports);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }

    }

}
