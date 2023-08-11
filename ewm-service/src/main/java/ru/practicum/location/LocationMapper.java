package ru.practicum.location;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location fromDto(LocationDto dto);

    LocationDto toDto(Location location);
}
