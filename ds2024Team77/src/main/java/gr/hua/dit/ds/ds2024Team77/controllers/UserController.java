package gr.hua.dit.ds.ds2024Team77.controllers;

import gr.hua.dit.ds.ds2024Team77.entities.Role;
import gr.hua.dit.ds.ds2024Team77.entities.User;
import gr.hua.dit.ds.ds2024Team77.repository.RoleRepository;
import gr.hua.dit.ds.ds2024Team77.repository.UserRepository;
import gr.hua.dit.ds.ds2024Team77.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
    @Controller
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

        @GetMapping("/register")
        public String register(Model model) {
            User newUser = new User();
            model.addAttribute("user", newUser);
            return "auth/register";
        }


        @PostMapping("/saveUser")
        public String saveUser(@ModelAttribute User user, Model model){
            System.out.println("Roles: "+user.getRoles());
            Long id = uService.saveUser(user);
            String message = "User '"+id+"' saved successfully !";
            model.addAttribute("msg", message);
            return "index";
        }

        @GetMapping("/users")
        public String showUsers(Model model){
            model.addAttribute("users", uService.getUsers());
            model.addAttribute("roles", roleRepository.findAll());

            return "auth/users";
        }

        @GetMapping("/user/{user_id}")
        public String showUser(@PathVariable Long user_id, Model model){
            model.addAttribute("user", uService.getUser(user_id));
            return "auth/user";
        }

        @GetMapping("/user/role/delete/{user_id}/{role_id}")
        public String deleteRolefromUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
            User user = uService.getUser(user_id).get();
            Role role = roleRepository.findById(role_id).get();
            user.getRoles().remove(role);
            System.out.println("Roles: "+user.getRoles());
            uService.updateUser(user);
            model.addAttribute("users", uService.getUsers());
            model.addAttribute("roles", roleRepository.findAll());
            return "auth/users";

        }

        @GetMapping("/user/role/add/{user_id}/{role_id}")
        public String addRoletoUser(@PathVariable Long user_id, @PathVariable Integer role_id, Model model){
            User user = uService.getUser(user_id).get();
            Role role = roleRepository.findById(role_id).get();
            user.getRoles().add(role);
            System.out.println("Roles: "+user.getRoles());
            uService.updateUser(user);
            model.addAttribute("users", uService.getUsers());
            model.addAttribute("roles", roleRepository.findAll());
            return "auth/users";

        }


        @GetMapping("/profile/{id}")
        public String showProfile(@PathVariable Long id, Model model){
            model.addAttribute("student", uService.getUser(id));
            return "student/student-profile";
        }

        //    @GetMapping("/{id}")
//    public String Show_UserProfile(@PathVariable Integer id, Model model){
//        users user_by_id = getUser(users_list,id);
//        model.addAttribute("projects", user_by_id);
//        return "user_show";
//    }

    }


