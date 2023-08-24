package ru.job4j.urlshortcut.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ClientRegisterDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Registration client entity")
public class ClientRegisterDTO {
    @Schema(description = "Is registration success or not")
    private boolean registration;
    @Schema(description = "Registered client username")
    private String username;
    @Schema(description = "Registered client password")
    private String password;
}
