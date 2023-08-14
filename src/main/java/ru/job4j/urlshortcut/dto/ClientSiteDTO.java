package ru.job4j.urlshortcut.dto;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Size;

/**
 * ClientSiteDTO.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 22.07.2023.
 */
public class ClientSiteDTO {
    @URL
    @Size(max = 100)
    String site;

    public ClientSiteDTO(String site) {
        this.site = site;
    }

    public ClientSiteDTO() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
