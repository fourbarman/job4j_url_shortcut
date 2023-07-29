package ru.job4j.urlshortcut.util;
/**
 * ShortcutNotFoundException.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
public class ShortcutNotFoundException extends RuntimeException {
    public ShortcutNotFoundException(String message) {
        super(message);
    }
}
