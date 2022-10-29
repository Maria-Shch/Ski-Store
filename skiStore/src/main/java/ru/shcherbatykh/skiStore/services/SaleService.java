package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.classes.PaymentResponse;
import ru.shcherbatykh.skiStore.models.Sale;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.repositories.SaleRepository;

@Service
public class SaleService {

    private final SaleRepository saleRepository;
    private final UserService userService;
    private final TransactionService transactionService;
    private final CartService cartService;
    private final InventoryService inventoryService;

    public SaleService(SaleRepository saleRepository, UserService userService, TransactionService transactionService,
                       CartService cartService, InventoryService inventoryService) {
        this.saleRepository = saleRepository;
        this.userService = userService;
        this.transactionService = transactionService;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public void addSale(Sale newSale) {
        saleRepository.save(newSale);
    }

    @Transactional
    public void submitOrder(PaymentResponse paymentResponse) {
        userService.updateUserPersonalData(paymentResponse.getUser());
        Transaction transaction = transactionService.addTransaction(new Transaction(paymentResponse.getUser()));
        for (CartElement cartElement : paymentResponse.getSelectedCartElements()){
            addSale(new Sale(null,
                    transaction,
                    cartElement.getCart().getInventory(),
                    cartElement.getModelOfInventory().getPrice(),
                    cartElement.getModelOfInventory().getDiscount(),
                    cartElement.getCart().getQuantity()));

            cartService.deleteCart(cartElement.getCart());
            inventoryService.inventorySale(cartElement.getCart().getInventory(), cartElement.getCart().getQuantity());
        }
    }
}
