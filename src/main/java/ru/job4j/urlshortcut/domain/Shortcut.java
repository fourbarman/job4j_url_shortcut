package ru.job4j.urlshortcut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * Shortcut.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 18.07.2023.
 */
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "shortcuts")
public class Shortcut {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "url")
    private String url;
    @Column(name = "code")
    private String code;
    @Column(name = "total")
    private long total;
    @Column(name = "client_id")
    private long clientId;
}
