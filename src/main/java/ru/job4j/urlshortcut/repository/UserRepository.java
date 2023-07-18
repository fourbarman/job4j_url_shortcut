package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.urlshortcut.domain.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    @NonNull
    List<User> findAll();
}
