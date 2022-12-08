package ru.practicum.shareit.booking.web.convertor;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.repository.Booking;
import ru.practicum.shareit.booking.web.dto.BookingDto;
import ru.practicum.shareit.item.repository.Item;
import ru.practicum.shareit.user.repository.User;

@Component
public class BookingDtoToBookingConverter implements Converter<BookingDto, Booking> {
    @Override
    public Booking convert(BookingDto source) {
        return Booking.builder()
                .id(source.getId())
                .start(source.getStart())
                .finish(source.getFinish())
                .status(source.getStatus())
                .renter(User.builder()
                        .id(source.getRenterId())
                        .build())
                .item(Item.builder()
                        .id(source.getItemId())
                        .build())
                .build();
    }
}
