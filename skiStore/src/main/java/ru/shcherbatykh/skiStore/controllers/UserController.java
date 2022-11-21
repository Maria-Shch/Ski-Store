package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.shcherbatykh.skiStore.classes.AggregationTransactionPrice;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.services.CityService;
import ru.shcherbatykh.skiStore.services.TransactionService;
import ru.shcherbatykh.skiStore.services.UserService;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final CityService cityService;

    public UserController(UserService userService, TransactionService transactionService, CityService cityService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.cityService = cityService;
    }

    @GetMapping("/account")
    public String getAccountPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        fillingModelForAccountPage(model, userService.getUserByUserDetails(userDetails));
        return "user/account";
    }

    @PostMapping("/account/update")
    public String updateUserPersonalData(Model model, @ModelAttribute("user") User user) {
        User updatedUser = userService.updateUserPersonalDataWithUsername(user);
        fillingModelForAccountPage(model, updatedUser);
        return "user/account";
    }

    private void fillingModelForAccountPage(Model model, User user){
        List<AggregationTransactionPrice> aggregationTransactionPrices
                = transactionService.getAggregationTransactionPrices(user);
        model.addAttribute("aggregationTransactionPrices", aggregationTransactionPrices);
        model.addAttribute("user", user);
        model.addAttribute("cities", cityService.getCities());
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUsersPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("users", userService.getUsersSorted());
        model.addAttribute("user", userService.getUserByUserDetails(userDetails));
        return "user/allUsers";
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getUserPage(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        User viewedUser = userService.getUser(id);
        List<AggregationTransactionPrice> aggregationTransactionPrices
                = transactionService.getAggregationTransactionPrices(viewedUser);
        model.addAttribute("aggregationTransactionPrices", aggregationTransactionPrices);
        model.addAttribute("user", userService.getUserByUserDetails(userDetails));
        model.addAttribute("viewedUser", viewedUser);
        return "user/userAccountForAdmin";
    }
}
