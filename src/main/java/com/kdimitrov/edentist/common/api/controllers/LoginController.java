package com.kdimitrov.edentist.common.api.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@AllArgsConstructor
@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String loginPage() {
        return "login/login";
    }

    @PostMapping("/login-error")
    public ModelAndView onLoginError(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String user) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.addObject("error", "bad_credentials");
        modelAndView.addObject("username", user);

        modelAndView.setViewName("login/login");

        return modelAndView;
    }

}
