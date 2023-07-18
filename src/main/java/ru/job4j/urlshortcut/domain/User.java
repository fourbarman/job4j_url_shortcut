package ru.job4j.urlshortcut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;
    @Column(name = "site")
    private String site;
    @OneToMany
    @JoinColumn(name = "user_id")
    private List<Shortcut> shortcuts = new ArrayList<>();

    public void addShortcut(Shortcut shortcut) {
        this.shortcuts.add(shortcut);
    }
}
