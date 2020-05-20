package org.codeme.im.imcommon.http;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class RestPagination implements Serializable {
    @JsonProperty("limit")
    private int limit;
    @JsonProperty("offset")
    private long offset;
    @JsonProperty("rows_found")
    private long rowsFound;

    public RestPagination() {
    }

    public RestPagination(int limit, long offset, long rowsFound) {
        this.limit = limit;
        this.offset = offset;
        this.rowsFound = rowsFound;
    }

    public Long getOffset() {
        return this.offset;
    }

    public Long getRowsFound() {
        return this.rowsFound;
    }

    public Integer getLimit() {
        return this.limit;
    }
}
