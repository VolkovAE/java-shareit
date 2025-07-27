package ru.practicum.shareit.booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.exception.ItemNotAvailable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

@Service
@Qualifier("BookingServiceImpl")
public class BookingServiceImpl implements BookingService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;

    private static final Logger log = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Autowired
    public BookingServiceImpl(ItemRepository itemRepository,
                              UserRepository userRepository,
                              BookingRepository bookingRepository,
                              BookingMapper bookingMapper) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
    }

    @Override
    public BookingDto add(final NewBookingRequest newBookingRequest, final Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException("Пользователь с id = " + userId + " не найден.", log));

        Item item = itemRepository.findById(newBookingRequest.getItemId()).orElseThrow(
                () -> new NotFoundException("Вещь с id = " + newBookingRequest.getItemId() + " не найдена.", log));

        item = itemRepository.findByIdAndAvailableTrue(newBookingRequest.getItemId()).orElseThrow(
                () -> new ItemNotAvailable("Вещь с id = " + newBookingRequest.getItemId() + " не найдена / не доступна для аренды.", log));

        Booking booking = bookingMapper.toBooking(newBookingRequest, item, user);

        booking = bookingRepository.save(booking);

        log.info("Добавлен новый запрос на аренду {}.", booking);

        return bookingMapper.toBookingDto(booking);
    }
}
