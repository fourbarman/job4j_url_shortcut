package ru.job4j.urlshortcut.dto;
/**
 * ShortcutStatisticDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
public class ShortcutStatisticDTO {
    private String url;
    private long total;

    public ShortcutStatisticDTO(String url, long total) {
        this.url = url;
        this.total = total;
    }

    public ShortcutStatisticDTO() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
