package com.anubhav.dev.store.mappers;

import com.anubhav.dev.store.dtos.OrderDto;
import com.anubhav.dev.store.entities.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
   OrderDto toOrderDto(Order order);
}
