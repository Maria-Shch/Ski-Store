package ru.shcherbatykh.skiStore.repositories;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.shcherbatykh.skiStore.models.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAll(Sort sort);

    User getUserById(long id);

    Optional<User> getUserByUsername(String email);
}
