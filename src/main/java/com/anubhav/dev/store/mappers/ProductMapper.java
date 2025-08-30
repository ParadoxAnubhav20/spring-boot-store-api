package com.anubhav.dev.store.mappers;

import com.anubhav.dev.store.dtos.ProductDto;
import com.anubhav.dev.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);
    @Mapping(target = "id", ignore = true)
    void updateProduct(ProductDto productDto, @MappingTarget Product product);
}
