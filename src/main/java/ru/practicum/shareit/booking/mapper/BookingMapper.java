package ru.practicum.shareit.booking.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoShort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDtoShort;
import ru.practicum.shareit.user.model.User;

import static ru.practicum.shareit.booking.state.BookingState.WAITING;

@UtilityClass
public class BookingMapper {
    public BookingDto bookingToBookingDto(Booking booking) {
        BookingDto bookingDto = new BookingDto();
        ItemDtoShort itemDtoShort = new ItemDtoShort();
        UserDtoShort userDtoShort = new UserDtoShort();
        bookingDto.setId(booking.getId());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        itemDtoShort.setId(booking.getItem().getId());
        itemDtoShort.setName(booking.getItem().getName());
        bookingDto.setItem(itemDtoShort);
        userDtoShort.setId(booking.getBooker().getId());
        bookingDto.setBooker(userDtoShort);
        bookingDto.setStatus(booking.getStatus());
        return bookingDto;
    }

    public Booking bookingCreateDtoToBooking(BookingCreateDto bookingCreateDto, ItemDto itemDto) {
        Booking booking = new Booking();
        User booker = new User();
        Item item = new Item();
        booking.setStart(bookingCreateDto.getStart());
        booking.setEnd(bookingCreateDto.getEnd());
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        booker.setId(bookingCreateDto.getBookerId());
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(WAITING);
        return booking;
    }
}