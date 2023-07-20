package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.repository.ShortcutRepository;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ShortcutService {
    private final ShortcutRepository shortcutRepository;

    @Transactional
    public Optional<Shortcut> findShortcutByCode(String code) {
        Shortcut sh = this.shortcutRepository.findByCode(code);
        if (sh != null) {
            this.shortcutRepository.incrementTotal(sh.getId());
        }
        return Optional.ofNullable(this.shortcutRepository.findByCode(code));
    }
}
