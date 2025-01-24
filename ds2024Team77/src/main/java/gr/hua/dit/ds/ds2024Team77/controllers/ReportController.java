package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.repository.ReportRepository;
import gr.hua.dit.ds.ds2024Team77.service.ReportService;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
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

    @GetMapping("")
    public List<Report> getReports(){
        return rService.getReports();
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Report> getReport(@PathVariable Long reportId){
        Optional<Report> report = rService.getReport(reportId);
        return report.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }

//    @GetMapping("/new")
//    public String createReport(Model model) {
//        Report report = new Report();
//        model.addAttribute("report",report ); // Στο attributeName δεν ξέρω αν θέλει report ή Report
//        return "report";
//    }

    @PostMapping("")
    public void createReport(@RequestBody Report report, @AuthenticationPrincipal UserDetailsImpl auth){
        report.setReporter(userService.getUser(auth.getId()).get());
        rService.saveReport(report);
    }

//    @PostMapping("/new")
//    public String saveReport(@ModelAttribute("/Report") Report report, Model model){
//        rService.saveReport(report);
//        model.addAttribute("Report", rService.getReport(report.getId()));
//        model.addAttribute("successMessage", "Report added successfully!");
//        rRepository.save(report); //Αυτή την γραμμή δεν ξέρω αν την χρειάζεται.
//        return "report";
//    }

    @GetMapping("/show")
    public String showReport(@PathVariable Long id , Model model){
        //model.addAttribute("reportList", report);
        return "report";
    }

}