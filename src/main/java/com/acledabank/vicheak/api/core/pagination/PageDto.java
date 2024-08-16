package com.acledabank.vicheak.api.core.pagination;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageDto {

    private List<?> content;
    private PaginationDto pagination;

    public PageDto(List<?> content, Page<?> page) {
        //return content of the page
        this.content = content;

        //build some common pagination info
        this.pagination = PaginationDto.builder()
                .pageNumber(page.getPageable().getPageNumber() + 1)
                .pageSize(page.getPageable().getPageSize())
                .totalPage(page.getTotalPages())
                .totalElement(page.getTotalElements())
                .numberOfElement(page.getNumberOfElements())
                .first(page.isFirst())
                .last(page.isLast())
                .empty(page.isEmpty())
                .build();
    }

}