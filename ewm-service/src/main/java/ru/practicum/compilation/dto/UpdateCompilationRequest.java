package ru.practicum.compilation.dto;

import lombok.*;

import javax.validation.constraints.Size;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCompilationRequest {
    private Set<Long> events;
    @Size(min = 1, max = 50)
    private String title;
    private boolean pinned;
}
