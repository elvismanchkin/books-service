package com.example.mapper;

import com.example.domain.Book;
import com.example.dto.BookDTO;
import jakarta.inject.Singleton;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Singleton
@Mapper(
        componentModel = MappingConstants.ComponentModel.JSR330,
        uses = CategoryMapper.class,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookMapper {

    BookDTO toDto(Book book);

    Book toEntity(BookDTO bookDTO);
}
