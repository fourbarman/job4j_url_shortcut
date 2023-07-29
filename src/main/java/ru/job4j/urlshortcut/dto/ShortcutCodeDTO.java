package ru.job4j.urlshortcut.dto;
/**
 * ShortcutCodeDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
public class ShortcutCodeDTO {
    private String code;

    public ShortcutCodeDTO(String code) {
        this.code = code;
    }

    public ShortcutCodeDTO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
