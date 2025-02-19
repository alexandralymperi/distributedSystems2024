package gr.hua.dit.ds.ds2024Team77.service;


import gr.hua.dit.ds.ds2024Team77.entities.FreelancerApplication;
import gr.hua.dit.ds.ds2024Team77.entities.ProjectApplications;
import gr.hua.dit.ds.ds2024Team77.repository.FreelancerApplicationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

//Service for managing freelancer applications (FreelancerApplication).
//It provides functions for saving, retrieving and deleting requests.
@Service
public class FreelancerApplicationService {

    private FreelancerApplicationRepository freelancerApplicationRepository;

    //FreelancerApplicationService constructor
    public FreelancerApplicationService(FreelancerApplicationRepository freelancerApplicationRepository) {
        this.freelancerApplicationRepository = freelancerApplicationRepository;
    }

    //Searches and returns a freelancer application based on its ID.
    @Transactional
    public Optional<FreelancerApplication> getFreelancerApplication(Long id) {
        return (freelancerApplicationRepository.findById(id));
    }

    @Transactional
    public void saveFreelancerApplication(FreelancerApplication freelancerApplication) {
        freelancerApplicationRepository.save(freelancerApplication);
    }

    @Transactional
    public List<FreelancerApplication> getFreelancerApplications() {
        return freelancerApplicationRepository.findAll();
    }

    @Transactional
    public boolean deleteApplication(@PathVariable Long applicationId) {

        Optional<FreelancerApplication> userOptional = this.freelancerApplicationRepository.findById(applicationId);

        if(userOptional.isEmpty()){
            return false;
        }else{
            this.freelancerApplicationRepository.deleteById(applicationId);
            return true;
        }

    }

}
