package com.fasfood.common.dto.response;

import com.fasfood.common.dto.PageDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

@Getter
@Setter
@NoArgsConstructor
public class PagingResponse<T> extends Response<List<T>> {

    private PageableResponse page = new PageableResponse();

    public PagingResponse(List<T> data, int pageIndex, int pageSize, long total) {
        this.page.setPageIndex(pageIndex);
        this.page.setPageSize(pageSize);
        this.page.setTotal(total);
        this.data = data;
        this.success();
    }

    public PagingResponse(PageDTO<T> pageInput) {
        this.data = pageInput.getData();
        this.page.setPageIndex(pageInput.getPage().getPageIndex());
        this.page.setPageSize(pageInput.getPage().getPageSize());
        this.page.setTotal(pageInput.getPage().getTotal());
        this.success();
    }

    public <U> PagingResponse(PageDTO<U> pageInput, Function<List<U>, List<T>> mapper) {
        PageDTO.PageableDTO pageable = pageInput.getPage();
        this.page.setPageIndex(pageable.getPageIndex());
        this.page.setPageSize(pageable.getPageSize());
        this.page.setTotal(pageable.getTotal());
        List<T> content = mapper.apply(pageInput.getData());
        if (content != null) {
            this.data = content;
        }
    }

    public static <T> PagingResponse<T> of(List<T> data, int pageIndex, int pageSize, long total) {
        return new PagingResponse<T>(data, pageIndex, pageSize, total);
    }

    public static <T> PagingResponse<T> of(PageDTO<T> pageInput) {
        return new PagingResponse<T>(pageInput);
    }

    public static <T> PagingResponse<T> failPaging(RuntimeException exception) {
        PagingResponse<T> response = new PagingResponse<T>();
        response.setSuccess(false);
        response.setException(exception);
        return response;
    }

    @Getter
    @Setter
    public static class PageableResponse implements Serializable {
        private int pageIndex;
        private int pageSize;
        private long total;
    }
}
