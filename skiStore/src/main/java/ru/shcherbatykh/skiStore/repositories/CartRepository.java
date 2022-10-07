package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.Cart;
import ru.shcherbatykh.skiStore.models.User;

import java.util.List;

@Repository
public interface CartRepository extends CrudRepository<Cart, Long> {

    List<Cart> getCartByUser(User user);
}
