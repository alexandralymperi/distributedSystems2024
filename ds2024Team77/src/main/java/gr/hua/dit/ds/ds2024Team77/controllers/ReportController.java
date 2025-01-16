package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Report;
import gr.hua.dit.ds.ds2024Team77.repository.ReportRepository;
import gr.hua.dit.ds.ds2024Team77.service.ReportService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/report")
public class ReportController {
    private ReportRepository rRepository;
    private ReportService rService;

    public ReportController(ReportRepository rRepository, ReportService rService) {
        this.rRepository = rRepository;
        this.rService = rService;
    }

    @GetMapping("/new")
    public String createReport(Model model) {
        Report report = new Report();
        model.addAttribute("report",report ); // Στο attributeName δεν ξέρω αν θέλει report ή Report
        return "report";
    }

    @PostMapping("/new")
    public String saveReport(@ModelAttribute("/Report") Report report, Model model){
        rService.saveReport(report);
        model.addAttribute("Report", rService.getReport(report.getId()));
        model.addAttribute("successMessage", "Report added successfully!");
        rRepository.save(report); //Αυτή την γραμμή δεν ξέρω αν την χρειάζεται.
        return "report";
    }

    @GetMapping("/show")
    public String showReport(@PathVariable Long id , Model model){
        Report report = rService.getReport(id);
        model.addAttribute("reportList", report);
        return "report";
    }

}