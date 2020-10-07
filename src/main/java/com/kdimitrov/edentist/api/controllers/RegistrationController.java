package com.kdimitrov.edentist.api.controllers;

import com.kdimitrov.edentist.api.services.UserService;
import com.kdimitrov.edentist.model.dto.RegistrationDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@AllArgsConstructor
@Controller
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/registration")
    public String showRegister(Model model) {
        model.addAttribute("formData", new RegistrationDTO());
        return "registration/registration";
    }

    @PostMapping("/registration")
    public String register(@Valid @ModelAttribute("formData") RegistrationDTO registrationDTO,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration/registration";
        }

        if (userService.exists(registrationDTO.getEmail())) {
            bindingResult.rejectValue("email",
                    "error.email",
                    "An account already exists for this email.");
            return "registration/registration";
        }

        userService.registerAndLoginUser(registrationDTO.getEmail(), registrationDTO.getPassword());

        return "redirect:/home";

    }

}
