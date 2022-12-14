package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.classes.AggregationTransactionPrice;
import ru.shcherbatykh.skiStore.classes.CartElement;
import ru.shcherbatykh.skiStore.classes.PaymentResponse;
import ru.shcherbatykh.skiStore.models.Sale;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.repositories.TransactionRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final SaleService saleService;
    private final CartService cartService;
    private final InventoryService inventoryService;

    public TransactionService(TransactionRepository transactionRepository, UserService userService, SaleService saleService, CartService cartService, InventoryService inventoryService) {
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.saleService = saleService;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public Transaction addTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

    @Transactional
    public Transaction getTransactionById(long id) {
        return transactionRepository.findById(id).orElse(null);
    }

    @Transactional
    public void submitOrder(PaymentResponse paymentResponse) {
        userService.updateUserPersonalData(paymentResponse.getUser());
        Transaction transaction = addTransaction(new Transaction(paymentResponse.getUser()));
        for (CartElement cartElement : paymentResponse.getSelectedCartElements()){
            saleService.addSale(new Sale(null,
                    transaction,
                    cartElement.getCart().getInventory(),
                    cartElement.getModelOfInventory().getPrice(),
                    cartElement.getModelOfInventory().getDiscount(),
                    cartElement.getCart().getQuantity()));

            cartService.deleteCart(cartElement.getCart());
            inventoryService.inventorySale(cartElement.getCart().getInventory(), cartElement.getCart().getQuantity());
        }
    }

    @Transactional
    public List<AggregationTransactionPrice> getAggregationTransactionPrices(User user){
        List<Transaction> transactions = user.getTransactions();
        return transactions.stream()
                .map(t -> new AggregationTransactionPrice(t, saleService.getResultPriceByTransaction(t)))
                .sorted(Comparator.comparing(AggregationTransactionPrice::getTransaction, Comparator.comparing(Transaction::getTime)).reversed())
                .toList();
    }

    @Transactional
    public AggregationTransactionPrice getAggregationTransactionPrice(Transaction transaction){
        return new AggregationTransactionPrice(transaction, saleService.getResultPriceByTransaction(transaction));
    }

    @Transactional
    public List<CartElement> getPurchasedCartElements(Transaction transaction){
        List<CartElement> cartElements = new ArrayList<>();
        for(Sale s : transaction.getSales()){
            CartElement ce = new CartElement(null, s.getInventory().getModelOfInventory(), s.getQuantity(), false, null, true);
            ce = inventoryService.setAttributeAndValueByInventory(ce, s.getInventory());
            cartElements.add(ce);
        }
        return cartElements;
    }
}
