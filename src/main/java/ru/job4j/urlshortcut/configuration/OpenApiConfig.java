package ru.job4j.urlshortcut.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

/**
 * OpenApiConfig.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 24.08.2023.
 */
@OpenAPIDefinition(info = @Info(
        title = "Url Shortcut Service Api",
        description = "Url Shortcut Service", version = "1.0.0",
        contact = @Contact(
                name = "Maksim Pankov",
                email = "maks.java@yandex.ru",
                url = "https://github.com/fourbarman"
        )
)
)
public class OpenApiConfig {
}
