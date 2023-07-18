package ru.job4j.urlshortcut.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.job4j.urlshortcut.domain.User;
import ru.job4j.urlshortcut.repository.ShortcutRepository;
import ru.job4j.urlshortcut.repository.UserRepository;

import java.util.List;
import java.util.Random;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ShortcutRepository shortcutRepository;

    public User save(String site) {
        User user = new User();
        user.setId(0);
        user.setUsername(this.generateRandom(8));
        user.setPassword(this.generateRandom(8));
        user.setSite(site);
        User savedUser = this.userRepository.save(user);
        return savedUser;
    }

    /**
     * Generate random string with given length.
     * Random symbols from {A-Z, a-z, 0-9}
     *
     * @param length Requested length.
     * @return Generated string.
     */
    private String generateRandom(int length) {
        Random rand = new Random();
        String str = rand.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(length)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
        return str;
    }

    public String convert(String url) {
        return this.generateRandom(10);
    }

    public List<User> getAll() {
        return this.userRepository.findAll();
    }
}
