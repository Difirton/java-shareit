package ru.practicum.shareit.item.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.repository.Comment;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.item.web.dto.CommentDto;
import ru.practicum.shareit.user.repository.User;

@Component
public class CommentDtoToCommentConverter implements Converter<CommentDto, Comment> {

    @Override
    public Comment convert(CommentDto source) {
        return Comment.builder()
                .text(source.getText())
                .created(source.getCreated())
                .item(Item.builder()
                        .id(source.getItemId())
                        .build())
                .author(User.builder()
                        .id(source.getAuthorId())
                        .build())
                .build();
    }
}
