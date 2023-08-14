package ru.job4j.urlshortcut.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.urlshortcut.UrlshortcutApplication;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.service.ClientService;
import ru.job4j.urlshortcut.service.ShortcutService;
import ru.job4j.urlshortcut.util.ShortcutNotFoundException;
import ru.job4j.urlshortcut.util.SiteRegisterException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = UrlshortcutApplication.class)
@AutoConfigureMockMvc
class ClientControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ClientService clientService;
    @MockBean
    ShortcutService shortcutService;

    /**
     * Test when register() and success then return status 200 and return json with credentials.
     *
     * @throws Exception Exception.
     */
    @Test
    void whenRegisterAndRegistrationIsSuccessfulThanReturnStatus200() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ClientSiteDTO clientSiteDTO = new ClientSiteDTO("http://test.com");
        ClientRegisterDTO clientRegisterDTO = new ClientRegisterDTO(true, "test", "testpass");
        when(clientService.save(any())).thenReturn(clientRegisterDTO);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientSiteDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(clientRegisterDTO)));
    }

    /**
     * Test when register() and site already exist then return status 409 and return json with exception.
     *
     * @throws Exception Exception.
     */
    @Test
    void whenRegisterAndSiteAlreadyExistsThanReturnStatus409AndThrowException() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ClientSiteDTO clientSiteDTO = new ClientSiteDTO("http://test.com");
        ClientRegisterDTO clientRegisterDTO = new ClientRegisterDTO(false, null, null);
        when(clientService.save(any())).thenReturn(clientRegisterDTO);
        mockMvc.perform(post("/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientSiteDTO)))
                .andExpect(status().isConflict())
                .andExpect(result ->
                        result.getResolvedException().getClass().equals(SiteRegisterException.class)
                );
    }

    /**
     * When convert() than return status 200 and json with code.
     *
     * @throws Exception Exception.
     */
    @Test
    @WithMockUser
    void convert() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ShortcutUrlDTO shortcutUrlDTO = new ShortcutUrlDTO("http://test.com/test/test");
        ShortcutCodeDTO shortcutCodeDTO = new ShortcutCodeDTO("xxxxxxxx");
        when(clientService.convert(any(), any())).thenReturn(shortcutCodeDTO);

        mockMvc.perform(post("/convert")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(shortcutUrlDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(shortcutCodeDTO)));
    }

    /**
     * Test when redirect() and URL was found by code then return status 200 and json with URL.
     *
     * @throws Exception
     */
    @Test
    void whenRedirectAndUrlFoundByCodeThenReturnStatus302AndReturnUrl() throws Exception {
        String code = "xxxxxxxxxx";
        String url = "http://test.com/test/test";
        ShortcutUrlDTO shortcutUrlDTO = new ShortcutUrlDTO(url);
        ObjectMapper objectMapper = new ObjectMapper();
        Optional<Shortcut> sh = Optional.of(new Shortcut(1L, url, code, 100L, 1L));
        when(shortcutService.findShortcutByCode(any())).thenReturn(sh);
        mockMvc.perform(get("/redirect/{code}", code))
                .andExpect(status().isFound())
                .andExpect(content().json(objectMapper.writeValueAsString(shortcutUrlDTO)));
    }

    /**
     * TEst when redirect() and URL was not found by code then return status 404 and json with exception.
     *
     * @throws Exception Exception.
     */
    @Test
    void whenRedirectAndUrlNotFoundByCodeThenReturnStatus404AndThrowException() throws Exception {
        String code = "xxxxxxxxxx";
        Optional<Shortcut> sh = Optional.empty();
        when(shortcutService.findShortcutByCode(any())).thenReturn(sh);
        mockMvc.perform(get("/redirect/{code}", code))
                .andExpect(status().isNotFound())
                .andExpect(result ->
                        result.getResolvedException().getClass().equals(ShortcutNotFoundException.class));
    }

    /**
     * Test statistic() then return status 200 and return json with List of URL and total.
     *
     * @throws Exception Exception.
     */
    @Test
    @WithMockUser
    void statistic() throws Exception {
        List<ShortcutStatisticDTO> statList = new ArrayList<>();
        statList.add(new ShortcutStatisticDTO("http://test.com/test", 10L));
        statList.add(new ShortcutStatisticDTO("http://test.com/test/test", 1L));
        when(clientService.getStatistics(any())).thenReturn(statList);
        mockMvc.perform(get("/statistic"))
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$[*].url",
                                containsInAnyOrder("http://test.com/test", "http://test.com/test/test")))
                .andExpect(jsonPath("$[*].total", containsInAnyOrder(10, 1)));
    }
}