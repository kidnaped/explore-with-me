package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Utils;
import ru.practicum.request.model.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParticipationRequestDto {
    private Long id;
    private Long event;
    private Long requester;
    private Status status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.FORMAT)
    private LocalDateTime created;
}
