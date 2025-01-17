package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Role;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


    /*@PostMapping("/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody User user) {
        Long id = uService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User '" + id + "' saved successfully!");
    }*/

    @GetMapping("/users")
    public List<User> showUsers() {
        return this.uService.getUsers();
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<User> showUser(@PathVariable Long user_id) {
        Optional<User> result = this.uService.getUser(user_id);
        return result.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{user_id}/role/{role_id}")
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

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable Long userId) {
        boolean result = this.uService.deleteStudentById(userId);
        if (result) {
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User deletion unsuccessful.");
        }
    }

    /*@GetMapping("/profile/{id}")
    public String showProfile(@PathVariable Long id, Model model){
        model.addAttribute("student", uService.getUser(id));
        return "student/student-profile";
    }*/

}


