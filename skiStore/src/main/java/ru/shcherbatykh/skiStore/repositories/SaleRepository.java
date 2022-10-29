package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Sale;

@Repository
public interface SaleRepository extends CrudRepository<Sale, Long> {

}
