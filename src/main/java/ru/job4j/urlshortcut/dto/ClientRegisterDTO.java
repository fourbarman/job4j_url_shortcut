package ru.job4j.urlshortcut.dto;

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
public class ClientRegisterDTO {
    private boolean registration;
    private String username;
    private String password;
}
