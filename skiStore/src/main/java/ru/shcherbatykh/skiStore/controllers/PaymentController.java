package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.classes.CartResponse;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.services.CartService;
import ru.shcherbatykh.skiStore.services.CityService;
import ru.shcherbatykh.skiStore.services.UserService;

import static ru.shcherbatykh.skiStore.utils.CommonUtils.decorationPrice;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final UserService userService;
    private final CityService cityService;
    private final CartService cartService;

    public PaymentController(UserService userService, CityService cityService, CartService cartService) {
        this.userService = userService;
        this.cityService = cityService;
        this.cartService = cartService;
    }

    @PostMapping
    public String getPaymentPage(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute("cartResponse") CartResponse cartResponse) {
        model.addAttribute("user", userService.getUserByUserDetails(userDetails));
        model.addAttribute("cities", cityService.getCities());
        model.addAttribute("resultPrice", decorationPrice(cartService.getResultPriceByCartResponse(cartResponse)));
        model.addAttribute("resultDiscountPrice", decorationPrice(cartService.getResultDiscountPriceByCartResponse(cartResponse)));
        model.addAttribute("selectedCartElements", cartResponse.getCartElements().stream().filter(CartElement::getSelected).toList());
        return "payment/payment";
    }

    @PostMapping("/confirm")
    public String getConfirmPaymentPage(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute("user") User user) {
        userService.updateUserPersonalData(user);
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        return "payment/confirm";
    }
}
