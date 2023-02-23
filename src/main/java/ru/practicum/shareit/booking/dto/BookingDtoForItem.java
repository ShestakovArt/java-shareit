package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@AllArgsConstructor
public class BookingDtoForItem {
    private Long id;
    private Long bookerId;
}