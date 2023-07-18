package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.ClientSiteDTO;
import ru.job4j.urlshortcut.service.ShortcutService;
import ru.job4j.urlshortcut.service.ClientService;

import java.util.List;

@RestController
@AllArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ShortcutService shortcutService;

    @PostMapping(value = "/registration", consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody ClientSiteDTO clientSiteDTO) {
        Client client = this.clientService.save(clientSiteDTO.getSite());
        return new ResponseEntity<>(client.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/convert", consumes = "application/json")
    public ResponseEntity<String> convert(@RequestBody String url) {

        String shortcut = this.clientService.convert(10);
        Client client = (Client)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        client.addShortcut(new Shortcut(0L, url, shortcut, 0, client.getId()));
        Client updatedClient = this.clientService.update(client);

        return new ResponseEntity<>(shortcut, HttpStatus.OK);
    }

    @GetMapping(value = "/redirect/{code}")
    public ResponseEntity<Shortcut> redirect(@PathVariable String code) {
        return new ResponseEntity<>(this.shortcutService.findShortcutByCode(code), HttpStatus.OK);
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<Client>> statistic() {
        List<Client> clients = this.clientService.getAll();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }
}
