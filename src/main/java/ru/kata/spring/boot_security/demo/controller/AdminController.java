package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleServiceImpl roleService;

    @Autowired
    public AdminController(UserService userService, RoleServiceImpl roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("")
    public String allUsersPage(@Valid @ModelAttribute("user") User user, Model model, Principal principal) {
        model.addAttribute("roles", roleService.getListOfRoles());
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        User autorUser = userService.findUserByLogin(principal.getName());
        model.addAttribute("autorUser", autorUser);
        return "admin_profile";
    }


    @PostMapping("/create")
    public String addUser(@Valid @ModelAttribute("user") User user,
                          BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getListOfRoles());
            return "new";
        } else {
            model.addAttribute("user", user);
            model.addAttribute("roles", roleService.getListOfRoles());
            userService.addUser(user);
            return "redirect:/admin";
        }
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

    @PostMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id", required = false) Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
