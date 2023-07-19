package ru.job4j.urlshortcut.dto;

public class ShortcutUrlDTO {
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
