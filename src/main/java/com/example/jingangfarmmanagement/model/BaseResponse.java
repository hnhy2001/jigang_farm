package com.example.jingangfarmmanagement.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseResponse  {
    private int code;
    private String message;
    private Object result;

    public BaseResponse(int statusCode, String status) {
        this.code = statusCode;
        this.message = status;
    }

    public BaseResponse success() {
        return this.success(null);
    }

    public BaseResponse success(Object result) {
        return BaseResponse.builder()
                .code(200)
                .message("Thành công!")
                .result(result)
                .build();
    }

    public BaseResponse fail(Object result) {
        return BaseResponse.builder()
                .code(500)
                .message("Thất bại!")
                .result(result)
                .build();
    }
}
