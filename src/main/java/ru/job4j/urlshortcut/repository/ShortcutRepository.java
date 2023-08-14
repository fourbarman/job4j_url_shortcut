package ru.job4j.urlshortcut.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.domain.Shortcut;

import java.util.Optional;

public interface ShortcutRepository extends CrudRepository<Shortcut, Long> {

    Optional<Shortcut> findByCode(String code);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Shortcut s SET s.total = s.total + 1 WHERE s.id = :id")
    void incrementTotal(Long id);
}
