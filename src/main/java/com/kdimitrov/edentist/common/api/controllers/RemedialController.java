package com.kdimitrov.edentist.common.api.controllers;

import com.kdimitrov.edentist.common.api.services.UserService;
import com.kdimitrov.edentist.common.api.services.VisitService;
import com.kdimitrov.edentist.common.model.UserEntity;
import com.kdimitrov.edentist.common.model.VisitEntity;
import com.kdimitrov.edentist.common.model.dto.VisitApprovalDto;
import javassist.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('REMEDIAL') || hasRole('ADMIN')")
public class RemedialController {

    private final UserService userService;
    private final VisitService visitService;

    @RequestMapping(value = "/visit/acknowledgement",
                    method = RequestMethod.GET)
    public String showVisits(Model model,
                             @AuthenticationPrincipal Principal principal) {

        UserEntity user = userService.getUser(principal.getName());
        if (user == null) {
            return "404";
        }

        List<VisitEntity> entities = visitService.getNotProcessed(user.getId());
        entities.sort(Comparator.comparing(VisitEntity::getDate));

        model.addAttribute("visitsToProcess", entities);
        return "remedial/remedial";
    }

    @RequestMapping(value = "/visit/acknowledgement",
                    method = RequestMethod.POST)
    public String approveVisit(@Valid @ModelAttribute("formData") VisitApprovalDto visitApprovalDto,
                               @AuthenticationPrincipal Principal principal,
                               BindingResult bindingResult) throws NotFoundException {

        if (bindingResult.hasErrors()) {
            return "home/home";
        }

        if (!userService.exists(principal.getName()) ||
                !userService.exists(visitApprovalDto.getRemedial())) {
            bindingResult.rejectValue("email",
                    "error.email",
                    "An account for this email is not found.");
            return "remedial/remedial";
        }

        visitService.changeStatus(visitApprovalDto);
        return "redirect:/visit/acknowledgement";
    }
}
