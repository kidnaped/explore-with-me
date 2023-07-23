package ru.practicum.user.service;

import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

import java.util.List;

public class UserServiceImpl implements UserService {
    @Override
    public UserDto register(UserDto dto) {
        return null;
    }

    @Override
    public User findById(Long userId) {
        return null;
    }

    @Override
    public List<UserDto> findAllOrByParams(List<Long> ids, Integer from, Integer size) {
        return null;
    }

    @Override
    public void deleteById(Long userId) {

    }
}
