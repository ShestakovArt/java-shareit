package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.markers.Marker;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;
    private final String pathId = "/{id}";
    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    @GetMapping
    public Collection<ItemDtoGetResponse> getAllItems(@RequestHeader(HEADER_USER_ID) Long ownerId) {
        return itemService.getAllForOwnerId(ownerId);
    }

    @GetMapping(pathId)
    public ItemDtoGetResponse getItemByIdForBooking(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                                    @PathVariable Long id) {
        return itemService.getItemByIdForBooking(id, ownerId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> findItems(@RequestParam String text) {
        return itemService.findItems(text);
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader(HEADER_USER_ID) Long ownerId,
                              @RequestBody @Validated(Marker.Create.class) ItemDto itemDto) {
        return itemService.createItem(itemDto, ownerId);
    }

    @PatchMapping(pathId)
    public ItemDto updateItem(@PathVariable Long id,
                              @RequestHeader(HEADER_USER_ID) Long ownerId,
                              @RequestBody @Validated(Marker.Update.class) ItemDto itemDto) {
        return itemService.updateItem(itemDto, ownerId, id);
    }

    @PostMapping(pathId + "/comment")
    public CommentDto createComment(@RequestHeader(HEADER_USER_ID) Long ownerId,
                                    @PathVariable Long id,
                                    @RequestBody @Validated(Marker.Create.class) CommentDto commentDto) {
        return itemService.createComment(id, ownerId, commentDto);
    }
}
