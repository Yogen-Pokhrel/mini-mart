package com.minimart.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseMeta {
    private int page;
    private int perPage;
    private int total;
    private int pageCount;
}
