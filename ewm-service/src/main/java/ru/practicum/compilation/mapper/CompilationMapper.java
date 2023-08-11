package ru.practicum.compilation.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.mapper.UserMapper;

import java.util.List;

@Component
@Mapper(componentModel = "spring",
        uses = {UserMapper.class, CategoryMapper.class, EventMapper.class, LocationMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CompilationMapper {
    CompilationDto toDto(Compilation compilation);

    List<CompilationDto> toDto(Iterable<Compilation> compilations);

    @Mapping(target = "events", ignore = true)
    Compilation fromDto(NewCompilationDto dto);

    @Mapping(target = "events", ignore = true)
    void fromDto(@MappingTarget Compilation compilation, UpdateCompilationRequest updateRequest);
}
