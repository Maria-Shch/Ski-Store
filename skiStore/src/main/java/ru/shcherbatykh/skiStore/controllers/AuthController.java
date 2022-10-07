package ru.shcherbatykh.skiStore.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.services.CityService;
import ru.shcherbatykh.skiStore.services.UserService;
import ru.shcherbatykh.skiStore.validator.UserValidator;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;
    private final CityService cityService;
    private final UserValidator userValidator;

    public AuthController(UserService userService, CityService cityService, UserValidator userValidator) {
        this.userService = userService;
        this.cityService = cityService;
        this.userValidator = userValidator;
    }

    @GetMapping("/index")
    public String getIndexPage() {
        return "catalog/index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "auth/login";
    }

    @GetMapping("/failureLogin")
    public String getFailureLoginPage(Model model) {
        model.addAttribute("hasError", true);
        return "auth/login";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("cities", cityService.getCities());
        return "auth/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("newUser") User newUser, BindingResult bindingResult,
                               Model model, @ModelAttribute("idSelectedCity") long idSelectedCity) {

        userValidator.validate(newUser, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("cities", cityService.getCities());
            return "auth/registration";
        }

        newUser.setCity(cityService.getCityById(idSelectedCity));
        userService.addUser(newUser);
        return "auth/login";
    }
}
