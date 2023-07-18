package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.repository.ShortcutRepository;
import ru.job4j.urlshortcut.repository.ClientRepository;

import java.util.List;
import java.util.Random;

import static java.util.Collections.emptyList;

@org.springframework.stereotype.Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {
    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder encoder;

    public Client save(String site) {
        Client client = new Client();
        client.setId(0);
        client.setUsername(this.generateRandom(8));
        String pass = this.generateRandom(8);
        System.out.println("Client pass: " + pass);
        client.setPassword(encoder.encode(pass));
        client.setSite(site);
        this.clientRepository.save(client);
        client.setPassword(pass);
        return client;
    }

    /**
     * Generate random string with given length.
     * Random symbols from {A-Z, a-z, 0-9}
     *
     * @param length Requested length.
     * @return Generated string.
     */
    private String generateRandom(int length) {
        Random rand = new Random();
        String str = rand.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(length)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
        return str;
    }

    public String convert(int length) {
        return this.generateRandom(length);
    }

    public List<Client> getAll() {
        return this.clientRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String clientName) throws UsernameNotFoundException {
        Client client = this.clientRepository.findClientByUsername(clientName);
        if (client == null) {
            throw new UsernameNotFoundException("Client not found");
        }
        return new User(client.getUsername(), client.getPassword(), emptyList());
    }

    public Client update(Client client) {
        return this.clientRepository.save(client);
    }
}
