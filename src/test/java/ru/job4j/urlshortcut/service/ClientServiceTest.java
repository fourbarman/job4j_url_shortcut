package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.repository.ClientRepository;
import ru.job4j.urlshortcut.util.ClientNotFoundException;
import ru.job4j.urlshortcut.util.UrlConvertException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
class ClientServiceTest {
    @InjectMocks
    ClientService clientService;
    @Mock
    ClientRepository clientRepository;
    @Mock
    BCryptPasswordEncoder encoder;
    String site = "http://site.com";
    String username = "testUsername";
    String pass = "testPassword";
    String validUrl = "http://site.com/site";
    String notValidUrl = "http://notValidSite.com/site";

    @BeforeEach
    void setSecurityContext() {
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void save() {
        ClientSiteDTO clientSiteDTO = new ClientSiteDTO(site);
        when(clientRepository.save(any()))
                .thenReturn(
                        new Client(0L, username, pass, site, new ArrayList<>()));
        ClientRegisterDTO clientRegisterDTO = clientService.save(clientSiteDTO);
        assertTrue(clientRegisterDTO.isRegistration());
        assertEquals(6, clientRegisterDTO.getUsername().length());
        assertEquals(8, clientRegisterDTO.getPassword().length());
        assertNotEquals(clientRegisterDTO.getUsername(), username);
        assertNotEquals(clientRegisterDTO.getPassword(), pass);
    }

    @Test
    void whenSaveAndSiteAlreadyExistThenRegisterFailAndReturnRegistrationFalse() {
        String site = "http://site.com";
        ClientSiteDTO clientSiteDTO = new ClientSiteDTO(site);
        when(clientRepository.save(any()))
                .thenThrow(ConstraintViolationException.class);
        ClientRegisterDTO clientRegisterDTO = clientService.save(clientSiteDTO);
        assertFalse(clientRegisterDTO.isRegistration());
        assertNull(clientRegisterDTO.getUsername());
        assertNull(clientRegisterDTO.getPassword());
    }

    @Test
    void whenConvertAndUserWasFoundAndUrlMatchesSiteNameAndUserSavedThenSuccess() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(validUrl);
        Client client = new Client(10L, username, pass, site, new ArrayList<>());

        when(clientRepository.findClientByUsername(any())).thenReturn(client);
        when(clientRepository.save(any())).thenReturn(client);

        ShortcutCodeDTO code = clientService.convert(shortcut);
        assertEquals(10, code.getCode().length());
        assertTrue(code.getCode().matches("[a-zA-Z0-9]+"));
    }

    @Test
    void whenConvertAndUserWasNotFoundThenThrowClientNotFoundException() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(validUrl);
        when(clientRepository.findClientByUsername(any())).thenReturn(null);
        ClientNotFoundException exception = Assertions.assertThrows(
                ClientNotFoundException.class, () ->
                        clientService.convert(shortcut)
        );
        Assertions.assertEquals("Client was not found", exception.getMessage());
    }

    @Test
    void whenConvertAndUserWasFoundAndUrlDoesntMatchSiteNameThenThrowUrlConvertException() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(notValidUrl);
        Client client = new Client(10L, username, pass, site, new ArrayList<>());

        when(clientRepository.findClientByUsername(any())).thenReturn(client);

        UrlConvertException exception = Assertions.assertThrows(
                UrlConvertException.class, () ->
                        clientService.convert(shortcut)
        );
        Assertions.assertEquals("URL should start with " + site, exception.getMessage());
    }

    @Test
    void whenConvertAndUserWasFoundAndUrlMatchesSiteNameAndUserWasNotSavedThenThrowUrlConvertException() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(validUrl);
        Client client = new Client(10L, username, pass, site, new ArrayList<>());

        when(clientRepository.findClientByUsername(any())).thenReturn(client);
        when(clientRepository.save(any())).thenReturn(null);

        UrlConvertException exception = Assertions.assertThrows(
                UrlConvertException.class, () ->
                        clientService.convert(shortcut)
        );
        Assertions.assertEquals("Url already exists", exception.getMessage());
    }


    @Test
    void whenGetStatisticsAndUserIsFoundThenSuccess() {
        Shortcut shortcut = new Shortcut(0L, validUrl, "xxxxxxxxxx", 0, 10);
        Client client = new Client(10L, username, pass, site, List.of(shortcut));

        when(clientRepository.findClientByUsername(any())).thenReturn(client);

        List<ShortcutStatisticDTO> statList = clientService.getStatistics();
        assertEquals(2, statList.size());
    }

    @Test
    void whenGetStatisticsAndUserIsNotFoundThenThrowClientNotFoundException() {
        when(clientRepository.findClientByUsername(any())).thenReturn(null);
        ClientNotFoundException exception = Assertions.assertThrows(
                ClientNotFoundException.class, () ->
                        clientService.getStatistics()
        );
        Assertions.assertEquals("Client not found", exception.getMessage());
    }

    @Test
    void whenLoadUserByUsernameAndSuccess() {
        when(clientRepository.findClientByUsername(username))
                .thenReturn(
                        new Client(0L, username, pass, site, new ArrayList<>()));
        UserDetails userDetails = clientService.loadUserByUsername(username);
        assertEquals(userDetails.getUsername(), username);
        assertEquals(userDetails.getPassword(), pass);
    }

    @Test
    void whenLoadUserByUsernameAndNotFoundThenTrowUserNotFoundException() {
        when(clientRepository.findClientByUsername(username))
                .thenReturn(null);
        UsernameNotFoundException exception = Assertions.assertThrows(
                UsernameNotFoundException.class, () ->
                        clientService.loadUserByUsername(username)
        );
        Assertions.assertEquals("Client not found", exception.getMessage());
    }
}