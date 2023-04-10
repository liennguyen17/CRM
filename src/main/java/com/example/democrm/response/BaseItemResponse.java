package com.example.democrm.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseItemResponse<T> extends BaseResponse {
    private T data;

    public BaseItemResponse(boolean success, Error error, T data) {
        super(success, error);
        this.data = data;
    }
}
