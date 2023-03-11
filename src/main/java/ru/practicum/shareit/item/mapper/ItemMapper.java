package ru.practicum.shareit.item.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDtoForItem;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoGetResponse;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@UtilityClass
public class ItemMapper {
    public Item itemDtoToItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(itemDto.getOwner());
        return item;
    }

    public ItemDto itemToItemDto(Item item) {
        ItemDto itemDto = new ItemDto();
        itemDto.setId(item.getId());
        itemDto.setName(item.getName());
        itemDto.setDescription(item.getDescription());
        itemDto.setAvailable(item.getAvailable());
        itemDto.setOwner(item.getOwner());
        return itemDto;
    }

    public Comment commentDtoToComment(CommentDto commentDto) {
        Comment comment = new Comment();
        User author = new User();
        comment.setId(commentDto.getId());
        comment.setText(commentDto.getText());
        author.setName(commentDto.getAuthorName());
        comment.setAuthor(author);
        comment.setCreated(commentDto.getCreated());
        return comment;
    }

    public CommentDto commentToCommentDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setText(comment.getText());
        commentDto.setAuthorName(comment.getAuthor().getName());
        commentDto.setCreated(comment.getCreated());
        return commentDto;
    }

    public ItemDtoGetResponse toItemDtoForBookingAndCommentShort(
            Item item, BookingDtoForItem lastBookingDto,
            BookingDtoForItem nextBookingDto, List<CommentDto> commentsDto) {
        ItemDtoGetResponse itemDtoGetResponse =
                new ItemDtoGetResponse();
        itemDtoGetResponse.setId(item.getId());
        itemDtoGetResponse.setName(item.getName());
        itemDtoGetResponse.setDescription(item.getDescription());
        itemDtoGetResponse.setAvailable(item.getAvailable());
        itemDtoGetResponse.setLastBooking(lastBookingDto);
        itemDtoGetResponse.setNextBooking(nextBookingDto);
        itemDtoGetResponse.setComments(commentsDto);
        return itemDtoGetResponse;
    }

    public BookingDtoForItem toBookingDtoForItem(Booking booking) {
        BookingDtoForItem bookingDtoForItem = new BookingDtoForItem();
        bookingDtoForItem.setId(booking.getId());
        bookingDtoForItem.setBookerId(booking.getBooker().getId());
        return bookingDtoForItem;
    }
}
