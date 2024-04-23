package com.example.jingangfarmmanagement.model.req;

import lombok.Data;

@Data
public class SearchReq {

    private String filter;

    private Integer page;

    private Integer size;

    private String sort;
}
