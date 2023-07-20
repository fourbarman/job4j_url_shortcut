package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.repository.ClientRepository;

import java.util.Optional;

import static java.util.Collections.emptyList;
@Slf4j
@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {
    private final ClientRepository clientRepository;

    public Optional<Client> save(Client client) {
        Optional<Client> opt = Optional.empty();
        try {
            opt = Optional.of(this.clientRepository.save(client));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return opt;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = this.clientRepository.findClientByUsername(username);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found");
        }
        return new User(client.getUsername(), client.getPassword(), emptyList());
    }

    public Optional<Client> findClientByUsername(String username) {
        return Optional.ofNullable(this.clientRepository.findClientByUsername(username));
    }
}
