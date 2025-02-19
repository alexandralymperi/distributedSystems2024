package gr.hua.dit.ds.ds2024Team77.service;

import gr.hua.dit.ds.ds2024Team77.entities.Role;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

//User Management Service (UserService).
//It provides functionalities for searching, storing, updating and authenticating users.
//It uses Spring Security to encrypt passwords and assign roles.
@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    //Constructor to initialize the UserService with the necessary repositories and the code cipher.
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    //Searches for a user in the database by ID.
    @Transactional
    public Optional<User> getUser(Long user_id){
        return userRepository.findById(user_id);
    }

    //Returns all users from the database.
    @Transactional
    public List<User> getUsers(){ return userRepository.findAll(); }

    //Saves a new user to the database and assigns roles to it.
    @Transactional
    public Long saveUser(User user, Boolean onApplication){

        String pswd = user.getPassword();
        String encodedPswd = passwordEncoder.encode(pswd);
        user.setPassword(encodedPswd);

        Role role = roleRepository.findByName("ROLE_BASIC")
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        //If the user is created through a request, the freelancer role is also added
        if(onApplication){

            Role freelancer = roleRepository.findByName("ROLE_FREELANCER")
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));

            userRoles.add(freelancer);
        }

        user.setRoles(userRoles);


        user = userRepository.save(user);
        return user.getId();
    }

    //Loads a user by username for Spring Security purposes.
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty()){
            throw new UsernameNotFoundException("User with username: " +username+" was not found.");
        }else{
            return UserDetailsImpl.build(opt.get());
        }

    }

    //Updates a user's information in the database.
    @Transactional
    public Long updateUser(User user){
        user = userRepository.save(user);
        return user.getId();
    }

    //Updates or inserts a new role into the database.
    @Transactional
    public void updateOrInsetRole(Role role){
        roleRepository.updateOrInsert(role);
    }


}
