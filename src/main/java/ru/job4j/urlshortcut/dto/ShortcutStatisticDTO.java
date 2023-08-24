package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Statistic entity")
public class ShortcutStatisticDTO {
    @Schema(description = "URL")
    private String url;
    @Schema(description = "URL redirects count")
    private long total;
}
