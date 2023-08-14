package ru.job4j.urlshortcut.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.job4j.urlshortcut.util.GenerateRandomInt;

/**
 * GenerateRandomIntConfiguration.
 * Injects values from application.properties.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 14.08.2023.
 */
@Configuration("application.properties")
public class GenerateRandomIntConfiguration {
    @Value("${username.length}")
    private Integer usernameLength;
    @Value("${pass.length}")
    private Integer passwordLength;
    @Value("${shortcut.code.length}")
    private Integer codeLength;

    @Bean
    public GenerateRandomInt generateRandomInt() {
        return new GenerateRandomInt(usernameLength, passwordLength, codeLength);
    }
}
