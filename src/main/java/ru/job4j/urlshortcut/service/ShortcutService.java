package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.repository.ShortcutRepository;

@Service
@AllArgsConstructor
public class ShortcutService {
    private final ShortcutRepository shortcutRepository;

    public Shortcut findShortcutByCode(String code) {
        return this.shortcutRepository.findByCode(code);
    }
}
