package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Role;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;
import gr.hua.dit.ds.ds2024Team77.service.UserDetailsImpl;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService uService;
    private UserRepository uRepository;
    private RoleRepository roleRepository;


    public UserController(UserService uService, UserRepository uRepository, RoleRepository roleRepository) {
        this.uService = uService;
        this.uRepository = uRepository;
        this.roleRepository = roleRepository;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/users")
    public List<User> showUsers() {
        return this.uService.getUsers();
    }


    @GetMapping("/{user_id}") //correct
    public ResponseEntity<User> showUser(@PathVariable Long user_id,
                                         @AuthenticationPrincipal UserDetailsImpl auth) {

        System.out.println("Requested user_id: " + user_id);
        System.out.println("Authenticated user_id: " + auth.getId());

        if (!user_id.equals(auth.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Optional<User> result = this.uService.getUser(user_id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{user_id}/role/{role_id}") //CORRECT
    public ResponseEntity<String> deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id) {
        Optional<User> existingUser = uService.getUser(user_id);
        Optional<Role> existingRole = roleRepository.findById(role_id);

        if (existingUser.isPresent() && existingRole.isPresent()) {

            User user = existingUser.get();
            Role role = existingRole.get();
            user.getRoles().remove(role);
            uService.updateUser(user);
            return ResponseEntity.ok("Role successfully removed from user.");

        } else {

            if (existingUser.isEmpty() && existingRole.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User and Role not found.");
            } else if (existingUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
            }

        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/{user_id}/role/{role_id}") //Correct
    public ResponseEntity<String> addRoletoUser(@PathVariable Long user_id, @PathVariable Integer role_id) {
        Optional<User> existingUser = uService.getUser(user_id);
        Optional<Role> existingRole = roleRepository.findById(role_id);

        if (existingUser.isPresent() && existingRole.isPresent()) {

            User user = existingUser.get();
            Role role = existingRole.get();
            user.getRoles().add(role);
            uService.updateUser(user);
            return ResponseEntity.ok("Role successfully added to user.");

        } else {

            if (existingUser.isEmpty() && existingRole.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User and Role not found.");
            } else if (existingUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Role not found.");
            }

        }

    }

}


