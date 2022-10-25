package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.models.Value;
import ru.shcherbatykh.skiStore.services.UserService;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final UserService userService;

    public CartController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add/{modelId}")
    public String addInventoryToCart(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable long modelId,
                                     @ModelAttribute("selectedValue") Value selectedValue) {
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        return "catalog/catalog"; //todo return to a page with model of inventory
    }
}
