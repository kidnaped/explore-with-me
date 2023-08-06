package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.dto.Utils;

import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class ApiError {
    private final String status;
    private final String reason;
    private final Throwable message;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.FORMAT)
    private final LocalDateTime timestamp;
}
