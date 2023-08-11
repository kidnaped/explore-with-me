package ru.practicum.event.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CategoryMapper.class, LocationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EventMapper {
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "category", ignore = true)
    Event fromDto(NewEventDto dto);

    @Mapping(target = "state", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "category", ignore = true)
    Event fromDto(@MappingTarget Event event, UpdateEventAdminRequest request);

    @Mapping(target = "state", ignore = true)
    @Mapping(target = "location", ignore = true)
    @Mapping(target = "category", ignore = true)
    Event fromDto(@MappingTarget Event event, UpdateEventUserRequest request);

    @Mapping(target = "views", ignore = true)
    EventFullDto toDto(Event event);

    @Mapping(target = "views", ignore = true)
    EventShortDto toShortDto(Event event);

    List<EventFullDto> toDto(Iterable<Event> events);

    List<EventShortDto> toShortDto(Iterable<Event> events);
}
