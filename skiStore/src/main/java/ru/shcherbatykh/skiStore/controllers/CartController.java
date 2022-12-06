package ru.shcherbatykh.skiStore.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.classes.CartResponse;
import ru.shcherbatykh.skiStore.models.Cart;
import ru.shcherbatykh.skiStore.services.CartService;
import ru.shcherbatykh.skiStore.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final UserService userService;
    private final CartService cartService;

    public CartController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }

    @GetMapping
    public String getCartPage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        fillingModelCartPage(model, userDetails);
        return "cart/cart";
    }

    @GetMapping("/deleteAll/{cartId}")
    public String deleteInventoryFromCart(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable long cartId) {
        Cart cart = cartService.getCartById(cartId);
        if(cart.getUser().getId() != userService.getUserByUserDetails(userDetails).getId()) {
            model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
            return "error/noAccess";
        }
        cartService.deleteCartById(cartId);
        fillingModelCartPage(model, userDetails);
        return "redirect:/cart";
    }

    @GetMapping("/{operation}/{cartId}")
    public String incrementQuantity(Model model, @AuthenticationPrincipal UserDetails userDetails, @PathVariable String operation,
                                    @PathVariable long cartId) {
        Cart cart = cartService.getCartById(cartId);
        if(cart.getUser().getId() != userService.getUserByUserDetails(userDetails).getId()) {
            model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
            return "error/noAccess";
        }
        cartService.changeQuantity(cart, operation, 1);
        fillingModelCartPage(model, userDetails);
        return "redirect:/cart";
    }

    private void fillingModelCartPage(Model model, UserDetails userDetails) {
        List<CartElement> cartElements = cartService.getCartElementsByUser(userService.getUserByUserDetails(userDetails));
        CartResponse cartResponse = CartResponse.builder()
                .cartElements(cartElements)
                .build();
        model.addAttribute("cartResponse", cartResponse);
        model.addAttribute("role", userService.getRoleByUserDetails(userDetails));
    }
}
