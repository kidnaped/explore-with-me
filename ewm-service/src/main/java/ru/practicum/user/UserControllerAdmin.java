package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Slf4j
public class UserControllerAdmin {
    private final UserService service;

    @PostMapping
    public UserDto register(@Valid @RequestBody UserDto dto,
                            HttpServletRequest request) {
        log.info("Received request from {} to URI {}",
                request.getRemoteAddr(), request.getRequestURI());
        return service.register(dto);
    }

    @GetMapping
    public List<UserDto> findAllOrByParams(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        log.info("Received request from {} to URI {}",
                request.getRemoteAddr(), request.getRequestURI());
        return service.findAllOrByParams(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        log.info("Received request from {} to URI {}",
                request.getRemoteAddr(), request.getRequestURI());
        service.deleteById(userId);
    }
}
