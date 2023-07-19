package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.repository.ClientRepository;

import java.util.List;

import static java.util.Collections.emptyList;

@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public Client save(Client client) {
        this.clientRepository.save(client);
        return client;
    }

    public List<Client> getAll() {
        return this.clientRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = this.clientRepository.findClientByUsername(username);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found");
        }
        return new User(client.getUsername(), client.getPassword(), emptyList());
    }

    public Client update(Client client) {
        return this.clientRepository.save(client);
    }

    public Client findClientByUsername(String username) {
        return this.clientRepository.findClientByUsername(username);
    }
}
