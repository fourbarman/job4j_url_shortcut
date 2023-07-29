package ru.job4j.urlshortcut.dto;

import org.hibernate.validator.constraints.URL;
/**
 * ShortcutUrlDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
public class ShortcutUrlDTO {
    @URL
    private String url;

    public ShortcutUrlDTO(String url) {
        this.url = url;
    }

    public ShortcutUrlDTO() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
