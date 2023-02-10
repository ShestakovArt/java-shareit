package ru.practicum.shareit.item.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ItemRepositoryImp implements ItemRepository {
    private final Map<Integer, Item> items = new HashMap<>();
    private static Integer id = 1;

    @Override
    public Collection<Item> getAllItemsForOwner(Integer ownerId) {
        log.info("Запрос items для пользователя с id = {}", ownerId);
        return items.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> findItems(String text) {
        log.info("Поиск items для запроса '{}'", text);
        return items.values().stream()
                .filter(item -> item.getName().toUpperCase().contains(text.toUpperCase()) ||
                        item.getDescription().toUpperCase().contains(text.toUpperCase()))
                .filter(item -> item.getAvailable().equals(true))
                .collect(Collectors.toList());
    }

    @Override
    public Item getItemById(Integer itemId) {
        log.info("Запрос item с id = {}", itemId);
        return items.get(itemId);
    }

    @Override
    public Item createItem(Item item) {
        item.setId(id++);
        items.put(item.getId(), item);
        log.info("Создан новй item с id = {}", item.getId());
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        items.put(item.getId(), item);
        log.info("Item с id {} обновлен", item.getId());
        return item;
    }
}
