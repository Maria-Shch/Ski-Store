package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Sale;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.repositories.SaleRepository;
import ru.shcherbatykh.skiStore.utils.CommonUtils;

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
    public Double getResultPriceByTransaction(Transaction transaction){
        List<Sale> sales = transaction.getSales();
        return sales.stream()
                .map(s -> CommonUtils.calculationPrice(s.getPrice(), s.getDiscount(), s.getQuantity()))
                .reduce(0.0, Double::sum);
    }
}
