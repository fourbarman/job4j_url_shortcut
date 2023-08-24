package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Shortcut URL entity")
public class ShortcutUrlDTO {
    @URL
    @Size(max = 100)
    @Schema(description = "URL")
    private String url;
}
