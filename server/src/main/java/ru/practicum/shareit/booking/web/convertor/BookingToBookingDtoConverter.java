package ru.practicum.shareit.booking.web.convertor;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.web.dto.BookingDto;
import ru.practicum.shareit.item.web.convertor.ItemToItemDtoConverter;
import ru.practicum.shareit.user.web.convertor.UserToUserDtoConverter;

@Component
@RequiredArgsConstructor
public class BookingToBookingDtoConverter implements Converter<Booking, BookingDto> {
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final ItemToItemDtoConverter itemToItemDtoConverter;

    @Override
    public BookingDto convert(Booking source) {
        return BookingDto.builder()
                .id(source.getId())
                .start(source.getStart())
                .finish(source.getFinish())
                .status(source.getStatus())
                .renterId(source.getRenter().getId())
                .itemId(source.getItem().getId())
                .booker(userToUserDtoConverter.convert(source.getRenter()))
                .item(itemToItemDtoConverter.convert(source.getItem()))
                .build();
    }
}
