package ru.job4j.urlshortcut.repository;

import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.domain.Shortcut;

public interface ShortcutRepository extends CrudRepository<Shortcut, Long> {
    Shortcut findByCode(String code);
}
