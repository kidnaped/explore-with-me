package ru.practicum.compilation.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCompilationRequest extends NewCompilationDto {
}
