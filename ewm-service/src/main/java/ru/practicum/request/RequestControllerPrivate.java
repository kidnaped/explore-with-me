package ru.practicum.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Utils;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.RequestService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestControllerPrivate {
    private final RequestService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable Long userId,
                                          @RequestParam Long eventId,
                                          HttpServletRequest request) {
        Utils.logForControllers(request);
        return service.register(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto update(@PathVariable Long userId,
                                          @PathVariable Long requestId,
                                          HttpServletRequest request) {
        Utils.logForControllers(request);
        return service.updateRequest(userId, requestId);
    }

    @GetMapping
    public List<ParticipationRequestDto> findByUserId(@PathVariable Long userId,
                                                      HttpServletRequest request) {
        Utils.logForControllers(request);
        return service.findByUserId(userId);
    }
}