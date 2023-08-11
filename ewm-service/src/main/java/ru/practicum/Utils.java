package ru.practicum;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import ru.practicum.exception.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Utils {
    public static final LocalDateTime START_TIME = LocalDateTime.of(2023, 1, 1, 0, 0, 0);
    public static final LocalDateTime END_TIME = LocalDateTime.of(2024, 1, 1, 0, 0, 0);

    public static PageRequest getPage(Integer from, Integer size) {
        if (from < 0 || size <= 0) {
            throw new ValidationException("FROM is below 0 or SIZE is below 1.");
        }
        return PageRequest.of(from / size, size, Sort.by("id").ascending());
    }

    public static void logForControllers(HttpServletRequest request) {
        log.info("Received request from {} to {}", request.getRequestURI(), request.getRemoteAddr());
    }
}
