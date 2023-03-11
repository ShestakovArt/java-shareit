package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.state.BookingState;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    @Query("select booking from Booking booking " +
            "where booking.booker.id = :ownerId " +
            "and :currentData > booking.end")
    List<Booking> findUserBookingItem(Long ownerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = :bookerId " +
            "and booking.status <> 'REJECTED' ")
    List<Booking> findUserBookingById(Long bookerId, Sort sort);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = :bookerId " +
            "and :currentData between booking.start and booking.end " +
            "order by booking.start desc")
    List<Booking> findUserBookingByCurrent(Long bookerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = :bookerId " +
            "and booking.end < :currentData " +
            "order by booking.start desc")
    List<Booking> findUserBookingByPast(Long bookerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = :bookerId " +
            "and booking.start > :currentData " +
            "order by booking.start desc")
    List<Booking> findUserBookingByFuture(Long bookerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.booker.id = :bookerId " +
            "and booking.status = :bookingStatus " +
            "order by booking.start desc")
    List<Booking> findUserBookingByStatus(Long bookerId, BookingState bookingStatus);

    @Query("select booking from Booking booking " +
            "where booking.item.owner = :ownerId " +
            "order by booking.start desc")
    List<Booking> findItemBookingById(Long ownerId);

    @Query("select booking from Booking booking " +
            "where booking.item.owner = :ownerId " +
            "and :currentData between booking.start and booking.end " +
            "order by booking.start desc")
    List<Booking> findItemBookingByCurrent(Long ownerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.item.owner = :ownerId " +
            "and booking.end < :currentData " +
            "order by booking.start desc")
    List<Booking> findItemBookingByPast(Long ownerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.item.owner = :ownerId " +
            "and booking.start > :currentData " +
            "order by booking.start desc")
    List<Booking> findItemBookingByFuture(Long ownerId, LocalDateTime currentData);

    @Query("select booking from Booking booking " +
            "where booking.item.owner = :ownerId " +
            "and booking.status = :status " +
            "order by booking.start desc")
    List<Booking> findItemBookingByStatus(Long ownerId, BookingState status);

    Collection<Booking> findByItemInAndStatus(List<Item> items, BookingState status, Sort start);

    List<Booking> findByItemIdAndStatus(Long itemId, BookingState approved, Sort start);
}