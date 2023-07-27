package ru.practicum.request.service;

import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto register(Long userId, Long eventId);

    ParticipationRequestDto updateRequest(Long userId, Long requestId);

    List<ParticipationRequestDto> findByUserId(Long userId);
}
