package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.Utils;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    @Transactional
    @Override
    public UserDto register(UserDto dto) {
        log.info("Received dto with params: ID {}, NAME {}, EMAIL {}",
                dto.getId(), dto.getName(), dto.getEmail());

        User user = repository.save(mapper.fromDto(dto));
        log.info("Registered user: {}, {}", user.getId(), user.getName());

        return mapper.toDto(user);
    }

    @Override
    public User findById(Long userId) {
        log.info("Received userID = {}", userId);

        User user = repository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with requested ID not found."));
        log.info("Found user = {}, {}", user.getId(), user.getName());

        return user;
    }

    @Override
    public List<UserDto> findAllOrByParams(List<Long> ids, Integer from, Integer size) {
        log.info("Received IDS size={}, FROM={}, SIZE={}",
                ids != null ? ids.size() : null, from, size);

        Pageable pages = Utils.getPage(from, size);
        List<User> users = ids == null ?
                repository.findAll(pages).toList() :
                repository.findAllByIdIn(ids, pages);
        log.info("Found {} users.", users.size());

        return mapper.toDto(users);
    }

    @Transactional
    @Override
    public void deleteById(Long userId) {
        log.info("Received userID = {} for deletion.", userId);

        User user = findById(userId);
        repository.deleteById(user.getId());

        log.info("Deleted user: {}, {}", user.getId(), user.getName());
    }
}
