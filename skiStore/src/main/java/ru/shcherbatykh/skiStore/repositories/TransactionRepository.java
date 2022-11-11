package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
