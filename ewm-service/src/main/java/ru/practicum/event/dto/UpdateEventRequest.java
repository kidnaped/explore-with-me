package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.dto.Utils;
import ru.practicum.location.LocationDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventRequest {
    @Size(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @Size(min = 20, max = 7000)
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = Utils.FORMAT)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, max = 120)
    private String title;

    @Override
    public String toString() {
        return "UpdateEventRequest{" +
                "category=" + category +
                ", eventDate=" + eventDate +
                ", title='" + title + '\'' +
                '}';
    }
}
