package ru.practicum.event.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.model.State;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventSearchRequestAdmin {
    private Set<Long> users;
    private Set<State> states;
    private Set<Long> categories;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Integer from;
    private Integer size;

    @Override
    public String toString() {
        return "EventSearchRequestAdmin{" +
                "rangeStart=" + rangeStart +
                ", rangeEnd=" + rangeEnd +
                '}';
    }
}
