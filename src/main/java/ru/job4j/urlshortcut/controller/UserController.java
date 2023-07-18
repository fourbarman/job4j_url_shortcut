package ru.job4j.urlshortcut.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.domain.User;
import ru.job4j.urlshortcut.dto.UserSiteDTO;
import ru.job4j.urlshortcut.service.ShortcutService;
import ru.job4j.urlshortcut.service.UserService;

import java.util.List;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final ShortcutService shortcutService;

    @PostMapping(value = "/registration", consumes = "application/json")
    public ResponseEntity<String> register(@RequestBody UserSiteDTO userSiteDTO) {
        User user = this.userService.save(userSiteDTO.getSite());
        return new ResponseEntity<>(user.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "/convert", consumes = "application/json")
    public ResponseEntity<String> convert(@RequestBody String url) {
        String shortcut = this.userService.convert(url);
        return new ResponseEntity<>(shortcut, HttpStatus.OK);
    }

    @GetMapping(value = "/redirect/{code}")
    public ResponseEntity<Shortcut> redirect(@PathVariable String code) {
        return new ResponseEntity<>(this.shortcutService.findShortcutByCode(code), HttpStatus.OK);
    }

    @GetMapping("/statistic")
    public ResponseEntity<List<User>> statistic() {
        List<User> users = this.userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
