package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    Collection<ItemDto> getAllItemsForOwner(Integer ownerId);

    ItemDto getItemById(Integer itemId);

    Collection<ItemDto> findItems(String text);

    ItemDto createItem(ItemDto itemDto, Integer ownerId);

    ItemDto updateItem(ItemDto itemDto, Integer ownerId, Integer itemId);
}
