package com.example.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
    private List<T> items;
    private long totalCount;
    private int totalPages;
    private int page;
    private int size;
    
    public static <T> PageDto<T> of(List<T> items, long totalCount, int page, int size) {
        int totalPages = size > 0 ? (int) Math.ceil((double) totalCount / size) : 0;
        return PageDto.<T>builder()
                .items(items)
                .totalCount(totalCount)
                .totalPages(totalPages)
                .page(page)
                .size(size)
                .build();
    }
}