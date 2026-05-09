package com.exam.student.dto;

import lombok.Data;

@Data
public class PageDTO<T> {
    private Long total;
    private Integer pageNum;
    private Integer pageSize;
    private Integer pages;
    private java.util.List<T> list;

    public static <T> PageDTO<T> of(com.github.pagehelper.Page<T> page) {
        PageDTO<T> dto = new PageDTO<>();
        dto.setTotal(page.getTotal());
        dto.setPageNum(page.getPageNum());
        dto.setPageSize(page.getPageSize());
        dto.setPages(page.getPages());
        dto.setList(page.getResult());
        return dto;
    }
}
