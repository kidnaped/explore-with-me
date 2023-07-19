package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.model.ViewStats;

@Mapper(componentModel = "spring")
@Component
public interface ViewStatsMapper {
    ViewStatsDto toDto(ViewStats stats);

    ViewStats fromDto(ViewStatsDto dto);
}
