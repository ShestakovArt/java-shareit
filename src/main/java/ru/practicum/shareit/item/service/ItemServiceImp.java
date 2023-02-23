package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;
import static ru.practicum.shareit.booking.state.BookingState.APPROVED;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImp implements ItemService {
    private final ItemRepository itemRepository;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    public final LocalDateTime CURRENT_DATA_TIME = LocalDateTime.now();

    @Override
    public List<ItemDtoGetResponse> getAllForOwnerId(Long ownerId) {
        List<Item> items = itemRepository
                .findByOwner(ownerId, Sort.by(ASC, "id"));
        Map<Item, List<Comment>> commentsMap = commentRepository
                .findByItemIn(items, Sort.by(ASC, "created"))
                .stream()
                .collect(groupingBy(Comment::getItem, toList()));
        Map<Item, List<Booking>> bookingsMap = bookingRepository
                .findByItemInAndStatus(items, APPROVED, Sort.by(DESC, "start"))
                .stream()
                .collect(groupingBy(Booking::getItem, toList()));
        List<ItemDtoGetResponse> itemDtoGetResponses =
                new ArrayList<>();
        for (Item item : items) {
            List<Booking> bookings = bookingsMap.getOrDefault(item, List.of());
            itemDtoGetResponses.add(ItemMapper.toItemDtoForBookingAndCommentShort(item, getLastBooking(bookings, item, ownerId),
                            getNextBooking(bookings, item, ownerId),
                            extractionComment(commentsMap, item)));
        }
        return itemDtoGetResponses;
    }

    @Override
    public ItemDto getItemById(Long itemId) {
        return ItemMapper.itemToItemDto(findItemById(itemId));
    }

    @Override
    public Collection<ItemDto> findItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.search(text).stream()
                .map(ItemMapper::itemToItemDto)
                .collect(toList());
    }

    @Override
    public ItemDtoGetResponse getItemByIdForBooking(Long itemId, Long ownerId) {
        Item item = findItemById(itemId);
        List<Booking> bookings = bookingRepository
                .findByItemIdAndStatus(itemId, APPROVED, Sort.by(DESC, "start"));
        List<CommentDto> comments = commentRepository
                .findByItemId(itemId, Sort.by(ASC, "created"))
                .stream().map(ItemMapper::commentToCommentDto).collect(toList());
        BookingDtoForItem lastBooking = getLastBooking(bookings, item, ownerId);
        BookingDtoForItem nextBooking = getNextBooking(bookings, item, ownerId);
        return ItemMapper.toItemDtoForBookingAndCommentShort(item, lastBooking, nextBooking, comments);
    }

    @Transactional
    @Override
    public CommentDto createComment(Long itemId, Long ownerId, CommentDto commentDto) {
        commentDto.setCreated(CURRENT_DATA_TIME);
        User author = userMapper.mapToUser(userService.getUserById(ownerId));
        Item item = findItemById(itemId);
        checkUserBooking(ownerId, itemId);
        return ItemMapper.commentToCommentDto(commentRepository
                .save(updateCommentData(commentDto, author, item)));
    }

    @Transactional
    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        itemDto.setOwner(ownerId);
        userService.getUserById(ownerId);
        itemDto.setId(itemRepository.save(ItemMapper.itemDtoToItem(itemDto)).getId());
        return itemDto;
    }

    @Transactional
    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId, Long itemId) {
        itemDto.setId(itemId);
        itemDto.setOwner(ownerId);
        checkOwner(itemDto, ownerId);
        Item itemOld = findItemById(itemId);
        Item itemNew = ItemMapper.itemDtoToItem(itemDto);
        return ItemMapper.itemToItemDto(updateItemData(itemNew, itemOld));
    }

    private BookingDtoForItem getLastBooking(List<Booking> bookings, Item item, Long ownerId) {
        Optional<Booking> lastBooking = bookings.stream()
                .filter(booking -> !booking.getStart().isAfter(LocalDateTime.now())).findFirst();
        if (lastBooking.isEmpty() || !item.getOwner().equals(ownerId)) {
            return null;
        }
        return ItemMapper.toBookingDtoForItem(lastBooking.get());
    }

    private BookingDtoForItem getNextBooking(List<Booking> bookings, Item item, Long ownerId) {
        Optional<Booking> nextBooking = bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now())).reduce((first, second) -> second);
        if (nextBooking.isEmpty() || !item.getOwner().equals(ownerId)) {
            return null;
        }
        return ItemMapper.toBookingDtoForItem(nextBooking.get());
    }

    private List<CommentDto> extractionComment(Map<Item, List<Comment>> commentsMap, Item item) {
        List<CommentDto> comments = List.of();
        if (!commentsMap.isEmpty()) {
            comments = commentsMap.get(item).stream()
                    .map(ItemMapper::commentToCommentDto)
                    .collect(toList());
        }
        return comments;
    }

    private Item findItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException(String.format(
                        "Вещь с id = %s не найдена", itemId)));
    }

    private void checkOwner(ItemDto itemDto, Long ownerId) {
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

    private void checkUserBooking(Long ownerId, Long itemId) {
        if (bookingRepository.findUserBookingItem(ownerId, LocalDateTime.now()).stream()
                .noneMatch(booking -> booking.getItem().getId().equals(itemId))) {
            throw new BadRequestException(String.format(
                    "Пользователь с id: %s не брал в аренду вещь с id: %s", ownerId, itemId));
        }
    }

    private Comment updateCommentData(CommentDto commentDto, User author, Item item) {
        Comment comment = ItemMapper.commentDtoToComment(commentDto);
        comment.setId(commentDto.getId());
        comment.setCreated(commentDto.getCreated());
        comment.setAuthor(author);
        comment.setItem(item);
        return comment;
    }
}
