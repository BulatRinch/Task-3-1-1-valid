package app.SpringBoot.controller;

import app.SpringBoot.model.User;
import app.SpringBoot.service.AppService;
import org.hibernate.AssertionFailure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.UnexpectedRollbackException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AppService appService;

    @Autowired
    public AdminController(AppService appService) {
        this.appService = appService;
    }

    @GetMapping({"", "list"})
    public String getAllUsers(Model model) {
        model.addAttribute("users", appService.findAllUsers());
        return "user-list";
    }

    @GetMapping(value = "/new")
    public String addUserForm(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("allRoles", appService.findAllRoles());
        return "user-form-add";
    }

    @GetMapping("/{id}/edit")
    public String editUserForm(@PathVariable(value = "id", required = true) Long userId, Model model) {
        try {
            model.addAttribute("user", appService.findUser(userId));
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return "redirect:/admin";
        }
        model.addAttribute("allRoles", appService.findAllRoles());
        return "user-form";
    }

    @PostMapping()
    public String saveUser(@Valid @ModelAttribute("user") User user,
                                   BindingResult bindingResult, Model model) {
        try {
            return appService.saveUser(user, bindingResult, model) ? "redirect:/admin" : "user-form";
        } catch (AssertionFailure | UnexpectedRollbackException e) {
            return "user-form";
        }
    }

    @PostMapping("/add")
    public String updateUser(@Valid @ModelAttribute("user") User user,
                                   BindingResult bindingResult, Model model) {
        try {
            return appService.saveUser(user, bindingResult, model) ? "redirect:/admin" : "user-form";
        } catch (AssertionFailure | UnexpectedRollbackException e) {
            return "user-form";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long userId) {
        appService.deleteUser(userId);

        return "redirect:/admin";
    }
}
