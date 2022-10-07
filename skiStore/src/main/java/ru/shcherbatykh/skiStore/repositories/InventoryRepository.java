package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Inventory;

import java.util.List;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {

    List<Inventory> findAll();

    Inventory getInventoryById(long id);
}
