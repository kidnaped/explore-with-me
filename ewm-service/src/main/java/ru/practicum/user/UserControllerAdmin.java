package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static ru.practicum.Utils.logForControllers;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@Validated
public class UserControllerAdmin {
    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@Valid @RequestBody UserDto dto,
                            HttpServletRequest request) {
        logForControllers(request);
        return service.register(dto);
    }

    @GetMapping
    public List<UserDto> findAllOrByParams(@RequestParam(required = false) List<Long> ids,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size,
                                           HttpServletRequest request) {
        logForControllers(request);
        return service.findAllOrByParams(ids, from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        logForControllers(request);
        service.deleteById(userId);
    }
}
