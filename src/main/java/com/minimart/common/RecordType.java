package com.minimart.common;

import lombok.Getter;

@Getter
public enum RecordType {
    string("string"),
    number("number"),
    select("select"),
    multiselect("multiselect");


    private final String value;

    RecordType(String value) {
        this.value = value;
    }

}
