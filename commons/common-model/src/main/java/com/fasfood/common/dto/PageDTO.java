package com.fasfood.common.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
public class PageDTO<T> implements Serializable {
    private PageableDTO page = new PageableDTO();
    private List<T> data;

    public PageDTO(List<T> data, int pageIndex, int pageSize, long total) {
        this.data = data;
        this.page.setPageIndex(pageIndex);
        this.page.setPageSize(pageSize);
        this.page.setTotal(total);
    }

    public <U> PageDTO(Page<U> pageInput, Function<List<U>, List<T>> mapper) {
        Pageable pageable = pageInput.getPageable();
        this.page.setPageIndex(pageable.getPageNumber());
        this.page.setPageSize(pageable.getPageSize());
        this.page.setTotal(pageInput.getTotalElements());
        List<T> content = mapper.apply(pageInput.getContent());
        if (content != null) {
            this.data = content;
        }
    }

    public static <T> PageDTO<T> of(List<T> data, int pageIndex, int pageSize, long total) {
        return new PageDTO<T>(data, pageIndex, pageSize, total);
    }

    public static <T> PageDTO<T> empty() {
        return new PageDTO<T>(new ArrayList<>(), 1, 30, 0L);
    }

    @Getter
    @Setter
    public static class PageableDTO {
        private int pageIndex = 0;
        private int pageSize = 0;
        private long total = 0L;
    }
}
