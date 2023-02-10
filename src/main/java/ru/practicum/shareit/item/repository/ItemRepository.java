package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Collection<Item> getAllItemsForOwner(Integer ownerId);

    Collection<Item> findItems(String text);

    Item getItemById(Integer itemId);

    Item createItem(Item item);

    Item updateItem(Item item);
}
