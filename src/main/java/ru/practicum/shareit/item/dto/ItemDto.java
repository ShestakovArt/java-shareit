package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.markers.Marker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class ItemDto {
    private Long id;
    @NotBlank(groups = {Marker.Create.class},
            message = "Имя не может быть пустым")
    private String name;
    @NotBlank(groups = {Marker.Create.class},
            message = "Описание не может быть пустым")
    private String description;
    @NotNull(groups = {Marker.Create.class},
            message = "Статус не может быть пустым")
    private Boolean available;
    private Long owner;
}