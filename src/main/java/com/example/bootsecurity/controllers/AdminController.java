package com.example.bootsecurity.controllers;

import com.example.bootsecurity.entity.Role;
import com.example.bootsecurity.entity.User;
import com.example.bootsecurity.service.RoleService;
import com.example.bootsecurity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class AdminController {
    private final UserDetailsService userDetailsService;
    private final UserService userService;

    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService, UserDetailsService userDetailsService) {
        this.userService = userService;
        this.roleService = roleService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping(value = "/admin")
    public String findAll(@AuthenticationPrincipal User user, Model model) {

        model.addAttribute("user", user);
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "index";
    }

//    @GetMapping(value = "/admin/{id}")
//    public String findById(@PathVariable("id") Long id, Model model) {
//        User user = userService.findById(id);
//        List<User> users = new ArrayList<>();
//        users.add(user);
//        model.addAttribute("users", users);
//        return "admin";
//    }

    @GetMapping("/admin/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        List<Role> roles = roleService.getRoleList();
        model.addAttribute("allRoles", roles);
        return "user-create";
    }

    @PostMapping("/admin/create")
    public String createUser(@ModelAttribute("user") User user) {
//        if(userDetailsService.loadUserByUsername(user.getEmail())!= null){
//            throw new RuntimeException("User with the email is exist");
//        }
        user.setRoles(Collections.singleton(new Role(1L)));
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }

//    @GetMapping("/admin/update/{id}")
//    public String updateUserForm(@PathVariable("id") Long id, Model model) {
//        User user = userService.findById(id);
//        model.addAttribute("user", user);
//        return "user-update";
//    }

    @PostMapping("/admin/{id}")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "select_roles", required = false) String[] role) {
        user.setRoles(user.getRoles());
        userService.update(user, role);
        return "redirect:/admin";
    }

}
