package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.Role;
import ru.shcherbatykh.skiStore.services.ModelService;
import ru.shcherbatykh.skiStore.services.UserService;

@Controller
@RequestMapping("/catalog")
public class CatalogController {

    private final ModelService modelService;
    private final UserService userService;

    public CatalogController(ModelService modelService, UserService userService) {
        this.modelService = modelService;
        this.userService = userService;
    }

    @GetMapping
    public String getCatalogPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails!=null)
            model.addAttribute("role", userService.findByUsername(userDetails.getUsername()).getRole());
        else
            model.addAttribute("role", Role.GUEST);

        model.addAttribute("modelTypes", modelService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping("/ski_poles")
    public String getSkiPolesPage(Model model) {
        model.addAttribute("modelTypes", modelService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping("/roller_skis")
    public String getRollerSkisPage(Model model) {
        model.addAttribute("modelTypes", modelService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping("/ski_boots")
    public String getSkiBootsPage( Model model) {
        model.addAttribute("modelTypes", modelService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping("/bindings")
    public String getBindingsPage( Model model) {
        model.addAttribute("modelTypes", modelService.getModelTypes());
        return "catalog/catalog";
    }

    @GetMapping("/roller_ski_poles")
    public String getRollerSkiPolesPage( Model model) {
        model.addAttribute("modelTypes", modelService.getModelTypes());
        return "catalog/catalog";
    }
}
