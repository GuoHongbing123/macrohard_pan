package com.vpsair.common.utils;

import java.util.List;

/**
 * @description : 分页DTO
 * @author : Shen Yuanfeng
 * @Date : 2021/6/10 20:17
 */
public class PageDTO<T> {

    private Integer page;

    private Integer limit;

    private Long total;

    private List<T> list;

    public PageDTO() {
    }

    public PageDTO(Integer page, Integer limit, Long total, List<T> list) {
        this.page = page;
        this.limit = limit;
        this.total = total;
        this.list = list;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "PageDTO{" +
                "page=" + page +
                ", limit=" + limit +
                ", total=" + total +
                ", list=" + list +
                '}';
    }
}
