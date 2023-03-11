package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingCreateDto {
    @NotNull(message = "id не может быть пустым")
    Long itemId;

    @NotNull(message = "Дата не может быть пустой")
    @FutureOrPresent(message = "Дата не может быть в прошлом")
    LocalDateTime start;

    @NotNull(message = "Дата не может быть пустой")
    @Future(message = "Дата не может быть в прошлом и равна текущей")
    LocalDateTime end;

    @JsonIgnore
    Long bookerId;
}