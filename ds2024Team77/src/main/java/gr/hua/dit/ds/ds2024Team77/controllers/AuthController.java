package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.config.JwtUtils;
import gr.hua.dit.ds.ds2024Team77.entities.Role;
import gr.hua.dit.ds.ds2024Team77.payload.JwtResponse;
import gr.hua.dit.ds.ds2024Team77.payload.LoginRequest;
import gr.hua.dit.ds.ds2024Team77.payload.MessageResponse;
import gr.hua.dit.ds.ds2024Team77.payload.SignupRequest;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import gr.hua.dit.ds.ds2024Team77.entities.User;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    RoleRepository roleRepository;
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    BCryptPasswordEncoder encoder;
    JwtUtils jwtUtils;

    public AuthController(RoleRepository roleRepository, AuthenticationManager authenticationManager,
                          UserRepository userRepository, BCryptPasswordEncoder encoder, JwtUtils jwtUtils) {
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
    }

    @PostConstruct
    public void setup() {
        Role role_user = new Role("ROLE_BASIC");
        Role roleFreelancer = new Role("ROLE_FREELANCER");
        Role role_admin = new Role("ROLE_ADMIN");

        roleRepository.updateOrInsert(role_user);
        roleRepository.updateOrInsert(roleFreelancer);
        roleRepository.updateOrInsert(role_admin);
    }

    /*@GetMapping("/login")
    public String login(){
        return "auth/login";
    }*/

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        System.out.println("authentication");

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        System.out.println("authentication: " + authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("post authentication");
        String jwt = jwtUtils.generateJwtToken(authentication);
        System.out.println("jwt: " + jwt);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,"Bearer",
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getUsername(),signUpRequest.getName(), signUpRequest.getSurname(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName("ROLE_BASIC")
                    .orElseThrow(() -> new RuntimeException("Error: Role BASIC is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                                .orElseThrow(() -> new RuntimeException("Error: Role ADMIN is not found."));
                        roles.add(adminRole);

                        break;
                    case "freelancer":
                        Role modRole = roleRepository.findByName("ROLE_FREELANCER")
                                .orElseThrow(() -> new RuntimeException("Error: Role FREELANCER is not found."));
                        roles.add(modRole);
                        break;
                    default:
                        Role userRole = roleRepository.findByName("ROLE_BASIC")
                                .orElseThrow(() -> new RuntimeException("Error: Role USER is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
