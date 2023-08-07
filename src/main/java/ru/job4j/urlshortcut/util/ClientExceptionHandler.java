package ru.job4j.urlshortcut.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * ClientExceptionHandler.
 *
 * @author fourbarman (maks.java@yandex.ru).
 * @version %I%, %G%.
 * @since 20.07.2023.
 */
@Slf4j
@ControllerAdvice
public class ClientExceptionHandler {
    @ExceptionHandler(value = SiteRegisterException.class)
    public ResponseEntity<Object> handle(SiteRegisterException sce) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiException apiException = new ApiException(
                sce.getMessage(),
                ZonedDateTime.now(ZoneId.of("UTC")),
                httpStatus
        );
        log.error(sce.getMessage(), sce);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = ShortcutNotFoundException.class)
    public ResponseEntity<Object> handle(ShortcutNotFoundException snfe) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = new ApiException(
                snfe.getMessage(),
                ZonedDateTime.now(ZoneId.of("UTC")),
                httpStatus
        );
        log.error(snfe.getMessage(), snfe);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = UrlConvertException.class)
    public ResponseEntity<Object> handle(UrlConvertException uce) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = new ApiException(
                uce.getMessage(),
                ZonedDateTime.now(ZoneId.of("UTC")),
                httpStatus
        );
        log.error(uce.getMessage(), uce);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<?> handle(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body(
                e.getFieldErrors().stream()
                        .map(f -> Map.of(
                                f.getField(),
                                String.format("%s. Actual value: %s", f.getDefaultMessage(), f.getRejectedValue())
                        ))
                        .collect(Collectors.toList())
        );
    }
}
