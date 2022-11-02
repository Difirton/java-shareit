package ru.practicum.shareit.item.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.web.dto.CommentDto;

@Component
public class CommentToCommentDtoConverter implements Converter<Comment, CommentDto> {
    @Override
    public CommentDto convert(Comment source) {
        return CommentDto.builder()
                .id(source.getId())
                .itemId(source.getItem().getId())
                .text(source.getText())
                .authorId(source.getAuthor().getId())
                .authorName(source.getAuthor().getName())
                .created(source.getCreated())
                .build();
    }
}
