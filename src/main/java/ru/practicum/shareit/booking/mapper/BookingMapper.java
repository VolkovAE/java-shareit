package ru.practicum.shareit.booking.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.NewBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.StatusBooking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.mapper.IgnoreUnmappedMapperConfig;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, config = IgnoreUnmappedMapperConfig.class)
@Component
public interface BookingMapper {
    BookingMapper INSTANCE = Mappers.getMapper(BookingMapper.class);

    @Mapping(ignore = true, target = "id")
    @Mapping(source = "request.start", target = "start", qualifiedByName = "toInstant")
    @Mapping(source = "request.end", target = "end", qualifiedByName = "toInstant")
    @Mapping(source = "item", target = "item")
    @Mapping(source = "booker", target = "booker")
    @Mapping(source = "request", target = "status", qualifiedByName = "getStatusWait")
    Booking toBooking(NewBookingRequest request, Item item, User booker);

    @Named("toInstant")
    default Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.toInstant(ZoneOffset.UTC);
    }

    @Named("getStatusWait")
    default StatusBooking getStatusWaiting(NewBookingRequest request) {
        return StatusBooking.WAITING;
    }

    @Mapping(source = "booking.start", target = "start", qualifiedByName = "toLocalDateTime")
    @Mapping(source = "booking.end", target = "end", qualifiedByName = "toLocalDateTime")
    @Mapping(source = "booking.status", target = "status", qualifiedByName = "getNameStatus")
    BookingDto toBookingDto(Booking booking);

    @Named("toLocalDateTime")
    default LocalDateTime toLocalDateTime(Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    @Named("getNameStatus")
    default String getNameStatusBooking(StatusBooking statusBooking) {
        return statusBooking.getValue();
    }
}
