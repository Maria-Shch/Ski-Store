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
import ru.shcherbatykh.skiStore.classes.PaymentResponse;
import ru.shcherbatykh.skiStore.services.CartService;
import ru.shcherbatykh.skiStore.services.CityService;
import ru.shcherbatykh.skiStore.services.SaleService;
import ru.shcherbatykh.skiStore.services.UserService;

import static ru.shcherbatykh.skiStore.utils.CommonUtils.decorationPrice;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    private final UserService userService;
    private final CityService cityService;
    private final CartService cartService;
    private final SaleService saleService;

    public PaymentController(UserService userService, CityService cityService, CartService cartService, SaleService saleService) {
        this.userService = userService;
        this.cityService = cityService;
        this.cartService = cartService;
        this.saleService = saleService;
    }

    @PostMapping
    public String getPaymentPage(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                 @ModelAttribute("cartResponse") CartResponse cartResponse) {

        PaymentResponse paymentResponse = PaymentResponse.builder()
                .selectedCartElements(cartResponse.getCartElements().stream().filter(CartElement::getSelected).toList())
                .user(userService.getUserByUserDetails(userDetails))
                .cities(cityService.getCities())
                .resultPrice(decorationPrice(cartService.getResultPriceByCartResponse(cartResponse)))
                .resultDiscountPrice(decorationPrice(cartService.getResultDiscountPriceByCartResponse(cartResponse)))
                .build();

        model.addAttribute("paymentResponse", paymentResponse);
        return "payment/payment";
    }

    @PostMapping("/confirm")
    public String getConfirmPaymentPage(Model model, @AuthenticationPrincipal UserDetails userDetails,
                                        @ModelAttribute("paymentResponse") PaymentResponse paymentResponse) {
        saleService.submitOrder(paymentResponse);
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
        model.addAttribute("paymentResponse", paymentResponse);
        return "payment/confirm";
    }
}
