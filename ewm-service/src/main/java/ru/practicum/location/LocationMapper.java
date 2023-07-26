package ru.practicum.location;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LocationMapper {
    Location fromDto(LocationDto dto);

    LocationDto toDto(Location location);
}
