package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.AggregationTransactionPrice;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.services.CityService;
import ru.shcherbatykh.skiStore.services.TransactionService;
import ru.shcherbatykh.skiStore.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;
    private final CityService cityService;

    public UserController(UserService userService, TransactionService transactionService, CityService cityService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.cityService = cityService;
    }

    @GetMapping
    public String getAccountPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        fillingModelForAccountPage(model, userService.getUserByUserDetails(userDetails));
        return "account/account";
    }

    @PostMapping("/update")
    public String updateUserPersonalData(Model model, @ModelAttribute("user") User user) {
        User updatedUser = userService.updateUserPersonalDataWithUsername(user);
        fillingModelForAccountPage(model, updatedUser);
        return "account/account";
    }

    private void fillingModelForAccountPage(Model model, User user){
        List<AggregationTransactionPrice> aggregationTransactionPrices
                = transactionService.getAggregationTransactionPrices(user);
        model.addAttribute("aggregationTransactionPrices", aggregationTransactionPrices);
        model.addAttribute("user", user);
        model.addAttribute("cities", cityService.getCities());
    }
}
