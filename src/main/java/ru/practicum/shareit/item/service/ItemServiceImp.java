package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImp implements ItemService {
    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final UserService userService;

    @Override
    public Collection<ItemDto> getAllItemsForOwner(Integer ownerId) {
        return itemRepository.getAllItemsForOwner(ownerId).stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto getItemById(Integer itemId) {
        return itemMapper.mapToItemDto(findItemById(itemId));
    }

    @Override
    public Collection<ItemDto> findItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.findItems(text).stream()
                .map(itemMapper::mapToItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemDto createItem(ItemDto itemDto, Integer ownerId) {
        itemDto.setOwner(ownerId);
        userService.getUserById(ownerId);
        itemDto.setId(itemRepository.createItem(itemMapper.mapToItem(itemDto)).getId());
        return itemDto;
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Integer ownerId, Integer itemId) {
        itemDto.setId(itemId);
        itemDto.setOwner(ownerId);
        checkOwner(itemDto, ownerId);
        Item itemOld = findItemById(itemId);
        Item itemNew = itemMapper.mapToItem(itemDto);
        return itemMapper.mapToItemDto(itemRepository.updateItem(updateItemData(itemNew, itemOld)));
    }

    private Item findItemById(Integer itemId) {
        Item item = itemRepository.getItemById(itemId);
        if (item == null) {
            throw new NotFoundException(String.format("Вещь с id = %s не найдена", itemId));
        }
        return item;
    }

    private void checkOwner(ItemDto itemDto, Integer ownerId) {
        if (!findItemById(itemDto.getId()).getOwner().equals(ownerId)) {
            throw new NotFoundException(
                    String.format("Пользователь с id = %s не является владельцем", ownerId));
        }
    }

    private Item updateItemData(Item itemNew, Item itemOld) {
        if (itemNew.getName() != null) {
            itemOld.setName(itemNew.getName());
        }
        if (itemNew.getDescription() != null) {
            itemOld.setDescription(itemNew.getDescription());
        }
        if (itemNew.getAvailable() != null) {
            itemOld.setAvailable(itemNew.getAvailable());
        }
        return itemOld;
    }
}
