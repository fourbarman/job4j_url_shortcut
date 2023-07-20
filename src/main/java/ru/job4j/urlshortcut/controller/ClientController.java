package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.service.ShortcutService;
import ru.job4j.urlshortcut.service.ClientService;
import ru.job4j.urlshortcut.util.ClientNotFoundException;
import ru.job4j.urlshortcut.util.ShortcutNotFoundException;
import ru.job4j.urlshortcut.util.SiteRegisterException;
import ru.job4j.urlshortcut.util.UrlConvertException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ShortcutService shortcutService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping(value = "/registration", consumes = "application/json")
    public ResponseEntity<ClientRegisterDTO> register(@RequestBody ClientSiteDTO clientSiteDTO) {
        String username = this.generateRandom(6);
        String pass = this.generateRandom(8);
        Optional<Client> client = this.clientService.save(
                new Client(0L, username, encoder.encode(pass), clientSiteDTO.getSite(), new ArrayList<>())
        );
        if (client.isEmpty()) {
            throw new SiteRegisterException("Site already exists");
        }
        return new ResponseEntity<>(new ClientRegisterDTO(true, username, pass), HttpStatus.OK);
    }

    @PostMapping(value = "/convert", consumes = "application/json")
    public ResponseEntity<ShortcutCodeDTO> convert(@RequestBody ShortcutUrlDTO shortcutUrlDTO) {
        String shortcut = this.generateRandom(10);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Client> client = this.clientService.findClientByUsername(username);

        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client was not found");
        }
        client.get().addShortcut(new Shortcut(0L, shortcutUrlDTO.getUrl(), shortcut, 0, client.get().getId()));
        Optional<Client> updatedClient = this.clientService.save(client.get());

        if (updatedClient.isEmpty()) {
            throw new UrlConvertException("Url already exists");
        }

        return new ResponseEntity<>(new ShortcutCodeDTO(shortcut), HttpStatus.OK);
    }

    @GetMapping(value = "/redirect/{code}")
    public ResponseEntity<ShortcutUrlDTO> redirect(@PathVariable String code) {
        Optional<Shortcut> sh = this.shortcutService.findShortcutByCode(code);
        if (sh.isEmpty()) {
            throw new ShortcutNotFoundException("URL with given code was not found");
        }
        return new ResponseEntity<>(new ShortcutUrlDTO(sh.get().getUrl()), HttpStatus.FOUND);
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<ShortcutStatisticDTO>> statistic() {
        List<ShortcutStatisticDTO> shortcuts = new ArrayList<>();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Client> client = this.clientService.findClientByUsername(username);
        if (client.isEmpty()) {
            throw new ClientNotFoundException("Client not found");
        }
        shortcuts.add(new ShortcutStatisticDTO(client.get().getSite(), 0));
        client.get().getShortcuts()
                .forEach(x -> shortcuts.add(new ShortcutStatisticDTO(x.getUrl(), x.getTotal())));

        return new ResponseEntity<>(shortcuts, HttpStatus.OK);
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
}
