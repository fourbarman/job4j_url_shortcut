package ru.job4j.urlshortcut.repository;


import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.job4j.urlshortcut.domain.Shortcut;

public interface ShortcutRepository extends CrudRepository<Shortcut, Long> {

    Shortcut findByCode(String code);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE Shortcut s SET s.total = s.total + 1 WHERE s.id = :id")
    void incrementTotal(Long id);
}
