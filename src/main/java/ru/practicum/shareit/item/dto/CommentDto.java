package ru.practicum.shareit.item.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class CommentDto {
    private Long id;
    @NotBlank(message = "Отзыв не может быть пустым")
    private String text;
    private String authorName;
    private LocalDateTime created;
}