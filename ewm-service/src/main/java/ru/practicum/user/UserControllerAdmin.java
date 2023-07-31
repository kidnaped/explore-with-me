package ru.practicum.user;

import lombok.RequiredArgsConstructor;
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
public class UserControllerAdmin {
    private final UserService service;

    @PostMapping
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
    public void deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        logForControllers(request);
        service.deleteById(userId);
    }
}
