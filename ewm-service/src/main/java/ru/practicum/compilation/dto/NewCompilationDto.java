package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCompilationDto {
    private Set<Long> events;
    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    private boolean pinned;

    @Override
    public String toString() {
        return "NewCompilationDto{" +
                "title='" + title + '\'' +
                ", pinned=" + pinned +
                '}';
    }
}
