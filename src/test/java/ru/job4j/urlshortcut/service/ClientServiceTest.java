package ru.job4j.urlshortcut.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.job4j.urlshortcut.UrlshortcutApplication;
import ru.job4j.urlshortcut.domain.Client;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.repository.ClientRepository;
import ru.job4j.urlshortcut.util.ClientNotFoundException;
import ru.job4j.urlshortcut.util.GenerateRandomInt;
import ru.job4j.urlshortcut.util.UrlConvertException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UrlshortcutApplication.class)
class ClientServiceTest {
    @InjectMocks
    ClientService clientService;
    @Mock
    ClientRepository clientRepository;
    @Mock
    BCryptPasswordEncoder encoder;
    @Mock
    GenerateRandomInt generateRandomInt;
    String testSite = "http://site.com";
    String testUsername = "testUsername";
    String testPass = "testPassword";
    String testCode = "testCode";
    String validUrl = "http://site.com/site";
    String notValidUrl = "http://notValidSite.com/site";

    Client client = new Client(0L, testUsername, testPass, testSite, new ArrayList<>());

    @BeforeEach
    void setGenerateRandomInt() {
        doReturn(testCode).when(generateRandomInt).generateCode();
        doReturn(testUsername).when(generateRandomInt).generateUsername();
        doReturn(testPass).when(generateRandomInt).generatePassword();
    }

    @Test
    void save() {
        ClientSiteDTO clientSiteDTO = new ClientSiteDTO(testSite);
        when(clientRepository.save(any()))
                .thenReturn(
                        new Client(0L, testUsername, testPass, testSite, new ArrayList<>()));

        ClientRegisterDTO clientRegisterDTO = clientService.save(clientSiteDTO);
        assertNotNull(clientRegisterDTO);
        assertTrue(clientRegisterDTO.isRegistration());
        assertEquals(clientRegisterDTO.getUsername(), testUsername);
        assertEquals(clientRegisterDTO.getPassword(), testPass);

        verify(clientRepository).save(any(Client.class));
        verify(generateRandomInt).generateUsername();
        verify(generateRandomInt).generatePassword();
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
        verify(clientRepository).save(any(Client.class));
    }

    @Test
    void whenConvertAndUserWasFoundAndUrlMatchesSiteNameAndUserSavedThenSuccess() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(validUrl);
        doReturn(Optional.of(client)).when(clientRepository).findClientByUsername(client.getUsername());
        doReturn(client).when(clientRepository).save(any());
        ShortcutCodeDTO shortcutCodeDTO = clientService.convert(shortcut, client.getUsername());
        assertEquals(testCode, shortcutCodeDTO.getCode());
        verify(clientRepository).findClientByUsername(any(String.class));
        verify(generateRandomInt).generateCode();
    }

    @Test
    void whenConvertAndUserWasNotFoundThenThrowClientNotFoundException() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(validUrl);
        doReturn(Optional.empty()).when(clientRepository).findClientByUsername(client.getUsername());
        ClientNotFoundException exception = Assertions.assertThrows(
                ClientNotFoundException.class, () ->
                        clientService.convert(shortcut, client.getUsername())
        );
        Assertions.assertEquals("Client was not found", exception.getMessage());
        verify(clientRepository).findClientByUsername(any(String.class));
        verifyNoInteractions(generateRandomInt);
    }

    @Test
    void whenConvertAndUserWasFoundAndUrlDoesntMatchSiteNameThenThrowUrlConvertException() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(notValidUrl);
        doReturn(Optional.of(client)).when(clientRepository).findClientByUsername(client.getUsername());
        UrlConvertException exception = Assertions.assertThrows(
                UrlConvertException.class, () ->
                        clientService.convert(shortcut, client.getUsername())
        );
        Assertions.assertEquals("URL should start with " + testSite, exception.getMessage());
        verify(clientRepository).findClientByUsername(any(String.class));
        verifyNoInteractions(generateRandomInt);
    }

    @Test
    void whenConvertAndUserWasFoundAndUrlMatchesSiteNameAndUserWasNotSavedThenThrowUrlConvertException() {
        ShortcutUrlDTO shortcut = new ShortcutUrlDTO(validUrl);
        doReturn(Optional.of(client)).when(clientRepository).findClientByUsername(client.getUsername());
        doReturn(null).when(clientRepository).save(client);

        UrlConvertException exception = Assertions.assertThrows(
                UrlConvertException.class, () ->
                        clientService.convert(shortcut, client.getUsername())
        );
        Assertions.assertEquals("Url already exists", exception.getMessage());
        verify(clientRepository).findClientByUsername(any(String.class));
        verify(clientRepository).save(client);
        verify(generateRandomInt).generateCode();
    }


    @Test
    void whenGetStatisticsAndUserIsFoundThenSuccess() {
        Shortcut shortcut = new Shortcut(0L, validUrl, testCode, 0, 10);
        client.addShortcut(shortcut);
        doReturn(Optional.of(client)).when(clientRepository).findClientByUsername(client.getUsername());
        List<ShortcutStatisticDTO> statList = clientService.getStatistics(client.getUsername());
        assertEquals(2, statList.size());
        verify(clientRepository).findClientByUsername(client.getUsername());
        verifyNoInteractions(generateRandomInt);
    }

    @Test
    void whenGetStatisticsAndUserIsNotFoundThenThrowClientNotFoundException() {
        doReturn(Optional.empty()).when(clientRepository).findClientByUsername(client.getUsername());
        ClientNotFoundException exception = Assertions.assertThrows(
                ClientNotFoundException.class, () ->
                        clientService.getStatistics(client.getUsername())
        );
        Assertions.assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findClientByUsername(any(String.class));
        verifyNoInteractions(generateRandomInt);
    }

    @Test
    void whenLoadUserByUsernameAndSuccess() {
        doReturn(Optional.of(client)).when(clientRepository).findClientByUsername(client.getUsername());
        UserDetails userDetails = clientService.loadUserByUsername(testUsername);
        assertEquals(userDetails.getUsername(), testUsername);
        assertEquals(userDetails.getPassword(), testPass);
        verify(clientRepository).findClientByUsername(any(String.class));
        verifyNoInteractions(generateRandomInt);
    }

    @Test
    void whenLoadUserByUsernameAndNotFoundThenTrowUserNotFoundException() {
        doReturn(Optional.empty()).when(clientRepository).findClientByUsername(testUsername);
        UsernameNotFoundException exception = Assertions.assertThrows(
                UsernameNotFoundException.class, () ->
                        clientService.loadUserByUsername(testUsername)
        );
        Assertions.assertEquals("Client not found", exception.getMessage());
        verify(clientRepository).findClientByUsername(any(String.class));
        verifyNoInteractions(generateRandomInt);
    }
}