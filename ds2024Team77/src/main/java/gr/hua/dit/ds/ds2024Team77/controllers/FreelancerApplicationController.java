package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.service.FreelancerApplicationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/FreelancerApplication")
public class FreelancerApplicationController {

    private FreelancerApplicationService freelancerApplicationService;

    public FreelancerApplicationController(FreelancerApplicationService freelancerApplicationService) {
        this.freelancerApplicationService = freelancerApplicationService;
    }

    @GetMapping("")
    public List<FreelancerApplication> getProjectApplications(){
        return freelancerApplicationService.getFreelancerApplications();
    }

    @GetMapping("/{freelancerapplicationId}")
    public ResponseEntity<FreelancerApplication> getProjectApplication(@PathVariable Long freelancerapplicationId){
        Optional<FreelancerApplication> freelancerapplication = freelancerApplicationService.getFreelancerApplication(freelancerapplicationId);
        return freelancerapplication.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.notFound().build());
    }




}
