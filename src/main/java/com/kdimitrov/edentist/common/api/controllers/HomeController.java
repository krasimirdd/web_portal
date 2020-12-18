package com.kdimitrov.edentist.common.api.controllers;

import com.kdimitrov.edentist.common.api.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('USER') || hasRole('ADMIN')")
public class HomeController {

    UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("dentists", userService.getAllDentists());
        return "home/home";
    }

    @GetMapping("/home")
    public String homeAbsolute(Model model) {
        return home(model);
    }

    @PostMapping("/home")
    public String homePost() {
        return "redirect:/home";
    }

}
