package ru.job4j.urlshortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ShortcutStatisticDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortcutStatisticDTO {
    private String url;
    private long total;
}
