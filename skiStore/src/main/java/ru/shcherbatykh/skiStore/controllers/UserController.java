package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.AggregationTransactionPrice;
import ru.shcherbatykh.skiStore.services.TransactionService;
import ru.shcherbatykh.skiStore.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/account")
public class UserController {

    private final UserService userService;
    private final TransactionService transactionService;

    public UserController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping
    public String getAccountPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        List<AggregationTransactionPrice> aggregationTransactionPrices
                = transactionService.getAggregationTransactionPrices(userService.getUserByUserDetails(userDetails));
        model.addAttribute("aggregationTransactionPrices", aggregationTransactionPrices);
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        return "account/account";
    }
}
