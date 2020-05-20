package org.codeme.im.imcommon.http;


import org.codeme.im.imcommon.http.util.JsonTools;

import java.io.Serializable;

/**
 * HZHttpResponse
 *
 * @author walker lee
 * @date 2019/6/27
 */
public class RestHttpResponse<T> implements Serializable {

    private RestMeta meta;
    private T data;
    private RestPagination pagination;

    public RestHttpResponse() {
    }

    public RestHttpResponse(Builder<T> builder) {
        this.meta = new RestMeta(builder.code, builder.message);
        this.data = builder.data;
        if (builder.isPagingResponse) {
            this.pagination = new RestPagination(builder.limit, builder.offset, builder.total);
        }
    }


    public static final class Builder<T> {
        private int code;
        private String message;
        private T data;
        private int limit;
        private long offset;
        private long total;
        private boolean isPagingResponse;

        public Builder dataResponse(T data) {
            this.code = 0;
            this.message = "";
            this.data = data;
            return this;
        }

        public Builder pageResponse(T data, int limit, long offset, long total) {
            this.code = 0;
            this.message = "";
            this.data = data;
            this.limit = limit;
            this.offset = offset;
            this.total = total;
            this.isPagingResponse = true;
            return this;
        }


        public Builder code(int code) {
            this.code = code;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder data(T data) {
            this.data = data;
            return this;
        }

        public Builder limit(int limit) {
            this.limit = limit;
            this.isPagingResponse = true;
            return this;
        }

        public Builder offset(long offset) {
            this.offset = offset;
            this.isPagingResponse = true;
            return this;
        }

        public Builder total(long total) {
            this.total = total;
            this.isPagingResponse = true;
            return this;
        }

        public RestHttpResponse build() {
            return new RestHttpResponse(this);
        }
    }

    public static Builder Builder() {
        return new Builder();
    }


    public T getData() {
        return data;
    }

    public RestPagination getPagination() {
        return pagination;
    }

    public String toResponse() {
        return JsonTools.toResponseStr(this);
    }

    public RestMeta getMeta() {
        return meta;
    }

    public boolean isSuccess() {
        return null != meta && meta.getCode() == 0;
    }
}


