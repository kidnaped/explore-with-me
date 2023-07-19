package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.Utils;
import ru.practicum.model.EndpointHit;

@Mapper(componentModel = "spring")
@Component
public interface EndpointHitMapper {
    @Mapping(source = "time", target = "timestamp", dateFormat = Utils.FORMAT)
    EndpointHitDto toDto(EndpointHit hit);

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "timestamp", target = "time", dateFormat = Utils.FORMAT)
    EndpointHit fromDto(EndpointHitDto dto);
}
