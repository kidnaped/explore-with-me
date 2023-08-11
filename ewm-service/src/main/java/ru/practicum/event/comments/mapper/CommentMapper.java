package ru.practicum.event.comments.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import ru.practicum.event.comments.dto.CommentDto;
import ru.practicum.event.comments.dto.NewCommentDto;
import ru.practicum.event.comments.model.Comment;

import java.util.List;

@Component
@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "event.id", target = "eventId")
    CommentDto toDto(Comment comment);

    @Mapping(source = "event.id", target = "eventId")
    @Mapping(source = "author.name", target = "author.name")
    List<CommentDto> toDto(Iterable<Comment> comments);

    Comment fromDto(NewCommentDto dto);

    @Mapping(target = "author", ignore = true)
    @Mapping(target = "event", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    Comment fromDto(@MappingTarget Comment comment, NewCommentDto dto);
}
