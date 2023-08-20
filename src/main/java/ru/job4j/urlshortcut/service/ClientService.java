package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.repository.ClientRepository;
import ru.job4j.urlshortcut.util.ClientNotFoundException;
import ru.job4j.urlshortcut.util.GenerateRandomInt;
import ru.job4j.urlshortcut.util.UrlConvertException;

import java.util.*;

import static java.util.Collections.emptyList;

/**
 * ClientService.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
@Slf4j
@Service
@AllArgsConstructor
public class ClientService implements UserDetailsService {

    private final ClientRepository clientRepository;
    private final BCryptPasswordEncoder encoder;
    private final GenerateRandomInt generateRandomInt;

    /**
     * Save Client to DB with random generated username, password.
     * 1. Generate random username and password.
     * 2. Save Client to DB.
     * 3.1. If save to DB throw Exception(i.e. Constraint exception) then log exception and return registration false.
     * 3.2. If save success then return clientRegisterDTO with generated username, password and registration true.
     *
     * @param clientSiteDTO ClientSiteDTO.
     * @return clientRegisterDTO.
     */
    public ClientRegisterDTO save(ClientSiteDTO clientSiteDTO) {
        String username = generateRandomInt.generateUsername();
        String pass = generateRandomInt.generatePassword();
        ClientRegisterDTO clientRegisterDTO = new ClientRegisterDTO(false, null, null);
        Client opt = null;
        try {
            opt = this.clientRepository.save(new Client(0L, username, encoder.encode(pass), clientSiteDTO.getSite(), new ArrayList<>()));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (opt != null) {
            clientRegisterDTO.setRegistration(true);
            clientRegisterDTO.setUsername(username);
            clientRegisterDTO.setPassword(pass);
        }
        return clientRegisterDTO;
    }

    /**
     * Convert URL to shortcut.
     * 1. Search Client by authentication.
     * 2. Get Client's site name.
     * 3. If Client was not found in DB then throw ClientNotFoundException.
     * 3.1. If given URL doesn't start with Client's site name, then throw UrlConvertException.
     * 3.2 If Client doesn't contain URL then generate code and save Client.
     * 3.3. If Client already contains URL then return its code.
     *
     * @param shortcutUrlDTO ShortcutUrlDTO.
     * @return ShortcutCodeDTO.
     */
    public ShortcutCodeDTO convert(ShortcutUrlDTO shortcutUrlDTO, String username) {
        Optional<Client> client = this.clientRepository.findClientByUsername(username);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client was not found");
        }
        String clientSite = client.get().getSite();
        if (!shortcutUrlDTO.getUrl().startsWith(clientSite)) {
            throw new UrlConvertException("URL should start with " + clientSite);
        }
        Shortcut present = null;
        for (Shortcut sc : client.get().getShortcuts()) {
            if (shortcutUrlDTO.getUrl().equals(sc.getUrl())) {
                present = sc;
            }
        }
        if (present == null) {
            String shortcut = generateRandomInt.generateCode();
            client.get().addShortcut(new Shortcut(0L, shortcutUrlDTO.getUrl(), shortcut, 0, client.get().getId()));
            this.clientRepository.save(client.get());
            return new ShortcutCodeDTO(shortcut);
        } else {
            return new ShortcutCodeDTO(present.getCode());
        }
    }

    /**
     * Get list of ShortcutStatisticDTO.
     * 1. Get Client from authentication.
     * 2.1. If Client was not found throw ClientNotFoundException.
     * 2.2. If Client found then get Shortcut List.
     *
     * @return ShortcutStatisticDTO list.
     */
    public List<ShortcutStatisticDTO> getStatistics(String username) {
        List<ShortcutStatisticDTO> statList = new ArrayList<>();
        Optional<Client> client = this.clientRepository.findClientByUsername(username);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }
        statList.add(new ShortcutStatisticDTO(client.get().getSite(), 0));
        client.get().getShortcuts()
                .forEach(x -> statList.add(new ShortcutStatisticDTO(x.getUrl(), x.getTotal())));

        return statList;
    }

    /**
     * Method for SpringSecurity authentication.
     *
     * @param username Username.
     * @return UserDetails.
     * @throws UsernameNotFoundException Exception.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Client> client = this.clientRepository.findClientByUsername(username);
        if (client.isEmpty()) {
            throw new UsernameNotFoundException("Client not found");
        }
        return new User(client.get().getUsername(), client.get().getPassword(), emptyList());
    }
}
