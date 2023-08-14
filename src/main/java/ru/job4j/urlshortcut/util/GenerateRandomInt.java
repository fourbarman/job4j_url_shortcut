package ru.job4j.urlshortcut.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Random;

/**
 * GenerateRandomInt.
 * Generates random String with given length.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 14.08.2023.
 */
@AllArgsConstructor
@NoArgsConstructor
public class GenerateRandomInt {
    private Integer usernameLength;
    private Integer passwordLength;
    private Integer codeLength;

    public String generateUsername() {
        return this.generateRandom(usernameLength);
    }

    public String generatePassword() {
        return this.generateRandom(passwordLength);
    }

    public String generateCode() {
        return this.generateRandom(codeLength);
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
        return rand.ints(48, 123)
                .filter(num -> (num < 58 || num > 64) && (num < 91 || num > 96))
                .limit(length)
                .mapToObj(c -> (char) c).collect(StringBuffer::new, StringBuffer::append, StringBuffer::append)
                .toString();
    }
}
