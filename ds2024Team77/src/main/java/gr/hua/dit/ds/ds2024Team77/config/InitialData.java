package gr.hua.dit.ds.ds2024Team77.config;

import gr.hua.dit.ds.ds2024Team77.entities.Project;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.ProjectRepository;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;
import gr.hua.dit.ds.ds2024Team77.service.ProjectService;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import gr.hua.dit.ds.ds2024Team77.entities.Role;

import java.util.Set;

@Configuration
public class InitialData {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitialData.class);

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProjectRepository projectRepository;

    private ProjectService projectService;
    public InitialData(UserRepository userRepository, RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.projectRepository = projectRepository;
    }


    @Transactional
    @PostConstruct
    public void populateDBWithInitialData(){

        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleBasicUser = new Role("ROLE_BASIC");
        Role roleFreelancer = new Role("ROLE_FREELANCER");

        roleAdmin = this.roleRepository.updateOrInsert(roleAdmin);
        roleBasicUser = this.roleRepository.updateOrInsert(roleBasicUser);
        roleFreelancer = this.roleRepository.updateOrInsert(roleFreelancer);

        var existing = this.userRepository.findByUsername("Admin").orElse(null);

        if(existing == null){

            LOGGER.info("Creating User 'admin'");
            User userAdmin = new User();

            userAdmin.setName("Admin");
            userAdmin.setSurname("Admin");
            userAdmin.setUsername("Admin");
            userAdmin.setPassword(this.passwordEncoder.encode("admin12345"));
            userAdmin.setEmail("it2022032@hua.gr");
            userAdmin.setRoles(Set.of(roleAdmin, roleBasicUser, roleFreelancer));

            this.userRepository.save(userAdmin);
        }

        var defaultUser = this.userRepository.findByUsername("miltos06").orElse(null);
        if(defaultUser == null){
            LOGGER.info("Creating user 'miltos'");

            defaultUser = new User();

            defaultUser.setName("Miltiadis");
            defaultUser.setSurname("Katarahias");
            defaultUser.setUsername("miltos06");
            defaultUser.setPassword(this.passwordEncoder.encode("admin12345"));
            defaultUser.setEmail("it2022058@hua.gr");
            defaultUser.setRoles(Set.of(roleBasicUser));

            this.userRepository.save(defaultUser);
        }

        var defaultFreelancer = this.userRepository.findByUsername("giorgos05").orElse(null);
        if(defaultFreelancer == null){
            LOGGER.info("Creating user 'giorgos'");

            defaultFreelancer = new User();

            defaultFreelancer.setName("Giorgos");
            defaultFreelancer.setSurname("Anastasiadis");
            defaultFreelancer.setUsername("giorgos05");
            defaultFreelancer.setPassword(this.passwordEncoder.encode("admin12345"));
            defaultFreelancer.setEmail("giorgos@gmail.com");
            defaultFreelancer.setRoles(Set.of(roleBasicUser, roleFreelancer));

            this.userRepository.save(defaultFreelancer);
        }


    }


}
