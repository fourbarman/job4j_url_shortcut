package ru.job4j.urlshortcut.util;

public class ShortcutNotFoundException extends RuntimeException {
    public ShortcutNotFoundException(String message) {
        super(message);
    }
}
