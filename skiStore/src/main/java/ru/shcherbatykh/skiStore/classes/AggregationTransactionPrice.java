package ru.shcherbatykh.skiStore.classes;

import lombok.Getter;
import ru.shcherbatykh.skiStore.models.Transaction;
import ru.shcherbatykh.skiStore.utils.CommonUtils;

@Getter
public class AggregationTransactionPrice {
    private Transaction transaction;
    private String dateForPrint;
    private String resultPriceForPrint;

    public AggregationTransactionPrice(Transaction transaction, Double resultPrice) {
        this.transaction = transaction;
        this.dateForPrint = CommonUtils.convertDateToStringForPrint(transaction.getTime());
        this.resultPriceForPrint = CommonUtils.decorationPrice(resultPrice);
    }
}
