package com.kdimitrov.edentist.common.api.controllers;

import com.kdimitrov.edentist.common.api.services.StatisticsService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@AllArgsConstructor
@Controller
@PreAuthorize("hasRole('ADMIN')")
public class StatsController {

    private final StatisticsService statisticsService;

    @GetMapping("stats")
    public String stats(Model model) {

        model.addAttribute("requestCount", statisticsService.getRequestCount());
        model.addAttribute("startedOn", statisticsService.getStartedOn());

        return "stats/stats";
    }

}
