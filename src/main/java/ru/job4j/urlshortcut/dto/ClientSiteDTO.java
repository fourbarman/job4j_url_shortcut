package ru.job4j.urlshortcut.dto;

public class ClientSiteDTO {
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
