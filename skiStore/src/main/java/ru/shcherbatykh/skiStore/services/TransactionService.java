package ru.shcherbatykh.skiStore.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.models.User;
import ru.shcherbatykh.skiStore.repositories.TransactionRepository;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public Transaction addTransaction(Transaction newTransaction) {
        return transactionRepository.save(newTransaction);
    }

    @Transactional
    public List<Transaction> getTransactionsByUser(User user) {
        return transactionRepository.findAllByUser(user);
    }
}
