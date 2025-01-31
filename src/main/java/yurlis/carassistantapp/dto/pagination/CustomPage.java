package yurlis.carassistantapp.dto.pagination;

import org.springframework.data.domain.Page;

import java.util.List;

public record CustomPage<T>(
    List<T> content,
    int page,
    int size,
    long totalElements
) {
    public static <T> CustomPage<T> from(Page<T> page) {
        return new CustomPage<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.getTotalElements()
        );
    }
}