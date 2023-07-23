package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public interface UserService {
    UserDto register(UserDto dto);

    User findById(Long userId);

    List<UserDto> findAllOrByParams(List<Long> ids, Integer from, Integer size);

    void deleteById(Long userId);
}
