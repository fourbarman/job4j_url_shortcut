package ru.job4j.urlshortcut.dto;

public class ClientRegisterDTO {
    private boolean registration;
    private String username;
    private String password;

    public ClientRegisterDTO(boolean registration, String username, String password) {
        this.registration = registration;
        this.username = username;
        this.password = password;
    }

    public ClientRegisterDTO() {
    }

    public boolean isRegistration() {
        return registration;
    }

    public void setRegistration(boolean registration) {
        this.registration = registration;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
