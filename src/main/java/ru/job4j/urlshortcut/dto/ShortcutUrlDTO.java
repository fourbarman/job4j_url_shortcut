package ru.job4j.urlshortcut.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;

/**
 * ShortcutUrlDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShortcutUrlDTO {
    @URL
    @Size(max = 100)
    private String url;
}
