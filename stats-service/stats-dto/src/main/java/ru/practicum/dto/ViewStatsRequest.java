package ru.practicum.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsRequest {
    private List<String> uris;
    private LocalDateTime start;
    private LocalDateTime end;
    private Boolean unique;
}
