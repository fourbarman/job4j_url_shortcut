package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.urlshortcut.domain.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @NonNull
    List<Client> findAll();

    Optional<Client> findClientByUsername(String username);
}
