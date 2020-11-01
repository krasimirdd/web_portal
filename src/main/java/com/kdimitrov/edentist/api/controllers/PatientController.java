package com.kdimitrov.edentist.api.controllers;

import com.kdimitrov.edentist.api.services.MQSender;
import com.kdimitrov.edentist.api.services.UserService;
import com.kdimitrov.edentist.api.services.VisitService;
import com.kdimitrov.edentist.model.Result;
import com.kdimitrov.edentist.model.UserEntity;
import com.kdimitrov.edentist.model.VisitEntity;
import com.kdimitrov.edentist.model.dto.VisitDTO;
import com.kdimitrov.rabbitmq.model.VisitRequest;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.security.Principal;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('USER')")
public class PatientController {

    private final UserService userService;
    private final VisitService visitService;
    private final MQSender<VisitRequest> mqService;

    @PostMapping("/patient/visit")
    public String bookVisit(@Valid @ModelAttribute("formData") VisitDTO visitDto,
                            @AuthenticationPrincipal Principal principal,
                            BindingResult bindingResult,
                            Model model) throws NotFoundException {

        if (bindingResult.hasErrors()) {
            return "home/home";
        }

        if (!userService.exists(visitDto.getRemedial())) {
            bindingResult.rejectValue("email",
                    "error.email",
                    "An dentist account does not exists for this email.");
            return "registration/registration";
        }

        Result<VisitRequest> visitResult = visitService.createVisit(visitDto, principal);
        if (visitResult.successful()) {
            mqService.send(visitResult.get());
            return "redirect:/home";
        } else {
            model.addAttribute("dentists", userService.getAllDentists());
            model.addAttribute("alternative",
                    visitResult.get().getDate()
                            .format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT)));
            return "home/home";
        }
    }

    @GetMapping("/patient/visit")
    public ModelAndView onLoginError() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("error", "bad_input");
        modelAndView.setViewName("home/home");

        return modelAndView;
    }

    @GetMapping("/patient/visit/track")
    public String listPatientVisitRequests(Model model,
                                           @AuthenticationPrincipal Principal principal) {

        UserEntity user = userService.getUser(principal.getName());
        if (user == null) {
            return "404";
        }
        List<VisitEntity> entities = visitService.getAllForPatient(user.getId());
        entities.sort(Comparator.comparing(VisitEntity::getDate));

        model.addAttribute("visits", entities);
        return "patient/patient";
    }
}
