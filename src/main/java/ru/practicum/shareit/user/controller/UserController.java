package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.markers.Marker;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final String pathId = "/{id}";

    @GetMapping
    public List<UserDto> getAll() {
        return userService.getAllUsers();
    }

    @GetMapping(pathId)
    public UserDto getById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDto create(@Validated(Marker.Create.class) @RequestBody UserDto userDto) {
        return userService.create(userDto);
    }

    @PatchMapping(pathId)
    public UserDto update(@RequestBody @Validated(Marker.Update.class) UserDto userDto,
                          @PathVariable Long id) {
        return userService.update(userDto, id);
    }

    @DeleteMapping(pathId)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
