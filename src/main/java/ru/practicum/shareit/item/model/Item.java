package ru.practicum.shareit.item.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString
@Entity
@Table(name = "items")
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @NotBlank(message = "Имя не может быть пустым")
    @Column
    String name;
    @NotBlank(message = "Описание не может быть пустым")
    @Column
    String description;
    @NotNull(message = "Статус не может быть пустым")
    @Column(name = "is_available")
    Boolean available;
    @Column(name = "owner_id")
    Long owner;
}
