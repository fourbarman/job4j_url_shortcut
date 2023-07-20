package ru.job4j.urlshortcut.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(message = "id cannot be null")
    private long id;

    @Column(name = "username")
    @NotBlank(message = "Login cannot be null")
    @Min(value = 6, message = "Login should have 6 or more symbols")
    private String username;

    @Column(name = "password")
    @NotBlank(message = "Password cannot be null")
    @Min(value = 6, message = "Password should have 8 or more symbols")
    private String password;

    @Column(name = "site")
    @URL()
    private String site;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    @ToString.Exclude
    private List<Shortcut> shortcuts = new ArrayList<>();

    public void addShortcut(Shortcut shortcut) {
        this.shortcuts.add(shortcut);
    }
}
