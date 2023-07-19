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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ShortcutService shortcutService;
    private final BCryptPasswordEncoder encoder;

    @PostMapping(value = "/registration", consumes = "application/json")
    public ResponseEntity<ClientRegisterDTO> register(@RequestBody ClientSiteDTO clientSiteDTO) {
        Client client = new Client();
        String pass = this.generateRandom(8);
        String username = this.generateRandom(6);
        client.setId(0L);
        client.setUsername(username);
        client.setPassword(encoder.encode(pass));
        client.setSite(clientSiteDTO.getSite());
        System.out.println("Client pass:" + client.getPassword());
        if (this.clientService.save(client) == null) {
            return new ResponseEntity<>(new ClientRegisterDTO(false, "", ""), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(new ClientRegisterDTO(true, username, pass), HttpStatus.OK);
    }

    @PostMapping(value = "/convert", consumes = "application/json")
    public ResponseEntity<ShortcutCodeDTO> convert(@RequestBody ShortcutUrlDTO shortcutUrlDTO) {

        String shortcut = this.generateRandom(10);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = this.clientService.findClientByUsername(username);

        client.addShortcut(new Shortcut(0L, shortcutUrlDTO.getUrl(), shortcut, 0, client.getId()));
        if (this.clientService.update(client) == null) {
            return new ResponseEntity<>(new ShortcutCodeDTO(), HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(new ShortcutCodeDTO(shortcut), HttpStatus.OK);
    }

    @GetMapping(value = "/redirect/{code}")
    public ResponseEntity<ShortcutUrlDTO> redirect(@PathVariable String code) {
        Shortcut sh = this.shortcutService.findShortcutByCode(code);
        return new ResponseEntity<>(new ShortcutUrlDTO(sh.getUrl()), HttpStatus.FOUND);
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<ShortcutStatisticDTO>> statistic() {
        List<ShortcutStatisticDTO> shortcuts = new ArrayList<>();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Client client = this.clientService.findClientByUsername(username);
        shortcuts.add(new ShortcutStatisticDTO(client.getSite(), 0));
        client.getShortcuts()
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
