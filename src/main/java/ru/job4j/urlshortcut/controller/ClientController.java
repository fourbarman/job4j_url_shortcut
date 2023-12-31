package ru.job4j.urlshortcut.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.job4j.urlshortcut.domain.Shortcut;
import ru.job4j.urlshortcut.dto.*;
import ru.job4j.urlshortcut.service.ShortcutService;
import ru.job4j.urlshortcut.service.ClientService;
import ru.job4j.urlshortcut.util.ClientNotFoundException;
import ru.job4j.urlshortcut.util.ShortcutNotFoundException;
import ru.job4j.urlshortcut.util.SiteRegisterException;
import ru.job4j.urlshortcut.util.UrlConvertException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

/**
 * ClientController.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 18.07.2023.
 */
@RestController
@AllArgsConstructor
@Tag(
        name = "Client controller",
        description = "Register URL, convert URL to shortcut, redirect URL by shortcut, statistics by shortcuts")
public class ClientController {
    private final ClientService clientService;
    private final ShortcutService shortcutService;

    /**
     * Register Site.
     *
     * @param clientSiteDTO ClientSiteDTO.
     * @return HttpStatus.OK if success or HttpStatus from SiteRegisterException.
     */
    @Operation(summary = "Register URL", description = "Register URL and get random username and password")
    @PostMapping(value = "/registration", consumes = "application/json")
    public ResponseEntity<ClientRegisterDTO> register(@RequestBody @Valid ClientSiteDTO clientSiteDTO) throws SiteRegisterException {
        ClientRegisterDTO clientRegisterDTO = this.clientService.save(clientSiteDTO);
        if (!clientRegisterDTO.isRegistration()) {
            throw new SiteRegisterException("Site already exists");
        }
        return new ResponseEntity<>(clientRegisterDTO, HttpStatus.OK);
    }

    /**
     * Convert URL to shortcut.
     *
     * @param shortcutUrlDTO ShortcutUrlDTO.
     * @return HttpStatus.OK if success or HttpStatus from UrlConvertException.
     */
    @Operation(summary = "Convert URL", description = "Convert URL to shortcut")
    @PostMapping(value = "/convert", consumes = "application/json")
    public ResponseEntity<ShortcutCodeDTO> convert(@RequestBody @Valid ShortcutUrlDTO shortcutUrlDTO) throws ClientNotFoundException, UrlConvertException {
        ShortcutCodeDTO code = this.clientService.convert(shortcutUrlDTO, getCurrentUserUsername());
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    /**
     * Return URL by code.
     *
     * @param code String code.
     * @return HttpStatus.FOUND if success or HttpStatus from ShortcutNotFoundException.
     */
    @Operation(summary = "Redirect by shortcut", description = "Redirect with URL by associated shortcut")
    @GetMapping(value = "/redirect/{code}")
    public ResponseEntity<ShortcutUrlDTO> redirect(
            @PathVariable @Parameter(description = "Shortcut") String code) throws ShortcutNotFoundException {
        Optional<Shortcut> sh = this.shortcutService.findShortcutByCode(code);
        if (sh.isEmpty()) {
            throw new ShortcutNotFoundException("URL with given code was not found");
        }
        return new ResponseEntity<>(new ShortcutUrlDTO(sh.get().getUrl()), HttpStatus.FOUND);
    }

    /**
     * Return statistic by site.
     *
     * @return HttpStatus.OK if success or HttpStatus from ClientNotFoundException.
     */
    @Operation(summary = "Get statistic", description = "Get statistic by registered site URL and shortcuts")
    @GetMapping("/statistic")
    public ResponseEntity<List<ShortcutStatisticDTO>> statistic() throws ClientNotFoundException {
        List<ShortcutStatisticDTO> statList = this.clientService.getStatistics(getCurrentUserUsername());
        return new ResponseEntity<>(statList, HttpStatus.OK);
    }

    private String getCurrentUserUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
