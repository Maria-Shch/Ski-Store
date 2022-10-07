package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Sale;
import ru.shcherbatykh.skiStore.models.Transaction;

import java.util.List;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Long> {

    List<Sale> getSaleByTransaction(Transaction transaction);
}
