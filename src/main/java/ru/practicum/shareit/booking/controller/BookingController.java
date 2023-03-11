package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingCreateDto;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.state.BookingState;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping("/{bookingId}")
    public BookingDto getById(@RequestHeader(HEADER_USER_ID) Long ownerId,
                              @PathVariable Long bookingId) {
        return bookingService.getById(bookingId, ownerId);
    }

    @GetMapping
    public List<BookingDto> getUserByStateParam(@RequestHeader(HEADER_USER_ID) Long bookerId,
                                                @RequestParam(defaultValue = "ALL")BookingState state) {
        return bookingService.getUserByStateParam(bookerId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getItemByStateParam(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                                @RequestParam(defaultValue = "ALL") BookingState state) {
        return bookingService.getItemByStateParam(ownerId, state);
    }

    @PostMapping
    public BookingDto create(@RequestHeader(HEADER_USER_ID) Long userId,
                             @RequestBody @Validated BookingCreateDto bookingCreateDto) {
        return bookingService.create(bookingCreateDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto confirmation(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                   @PathVariable Long bookingId,
                                   @RequestParam Boolean approved) {
        return bookingService.confirmation(ownerId, bookingId, approved);
    }
}