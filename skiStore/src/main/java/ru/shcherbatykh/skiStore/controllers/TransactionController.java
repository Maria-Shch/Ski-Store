package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.AggregationTransactionPrice;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.services.TransactionService;
import ru.shcherbatykh.skiStore.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/transaction")
public class TransactionController {

    private final UserService userService;
    private final TransactionService transactionService;

    public TransactionController(UserService userService, TransactionService transactionService) {
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/{id}")
    public String getHistoryTransactionPage(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable long id) {
        Transaction transaction = transactionService.getTransactionById(id);
        AggregationTransactionPrice aggregationTransactionPrice = transactionService.getAggregationTransactionPrice(transaction);
        List<CartElement> purchasedCartItems = transactionService.getPurchasedCartElements(transaction);
        model.addAttribute("aggregationTransactionPrice", aggregationTransactionPrice);
        model.addAttribute("purchasedCartItems", purchasedCartItems);
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        return "transaction/transaction";
    }
}
