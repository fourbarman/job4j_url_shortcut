package ru.job4j.urlshortcut.dto;

public class UserSiteDTO {
    String site;

    public UserSiteDTO(String site) {
        this.site = site;
    }

    public UserSiteDTO() {
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
