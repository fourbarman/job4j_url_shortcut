package ru.job4j.urlshortcut.dto;

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
