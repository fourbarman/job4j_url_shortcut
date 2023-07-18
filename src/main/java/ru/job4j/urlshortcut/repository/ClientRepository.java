package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.job4j.urlshortcut.domain.Client;

import java.util.List;

public interface ClientRepository extends CrudRepository<Client, Long> {
    @NonNull
    List<Client> findAll();

    Client findUserByUsername(String username);

    Client findClientByUsername(String clientName);
}
