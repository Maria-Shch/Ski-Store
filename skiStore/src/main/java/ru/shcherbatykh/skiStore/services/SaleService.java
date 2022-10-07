package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Sale;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.repositories.SaleRepository;

import java.util.List;

@Service
public class SaleService {

    private final SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    @Transactional
    public void addSale(Sale newSale) {
        saleRepository.save(newSale);
    }

    @Transactional
    public List<Sale> getSalesByTransaction(Transaction transaction) {
        return saleRepository.getSaleByTransaction(transaction);
    }
}
