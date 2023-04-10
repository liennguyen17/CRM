package com.example.democrm.controller;

import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.response.BaseItemResponse;
import com.example.democrm.response.BaseListItemResponse;
import com.example.democrm.response.BaseResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class BaseController {

//    protected <T> ResponseEntity buildItemResponse(T data) {
//        BaseItemResponse<T> response = new BaseItemResponse<>();
//        if (data != null) {
//            response.setData(data);
//            response.setSuccess(true);
//            return ResponseEntity.ok(response);
//        } else {
//            response.setSuccess(false);
//            response.setFailed(ErrorCodeDefs.NOT_FOUND, ErrorCodeDefs.getErrMsg(ErrorCodeDefs.NOT_FOUND));
//            return ResponseEntity.ok(response);
//        }
//    }

    protected <T> ResponseEntity<?> buildItemResponse(T data) {
        BaseItemResponse<T> response = new BaseItemResponse<>();
        response.setData(data);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    protected <T> ResponseEntity<?> buildListItemResponse(List<T> data, long total) {
        BaseListItemResponse<T> response = new BaseListItemResponse<>();
        response.setResult(data, total);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    protected ResponseEntity<?> buildResponse() {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setSuccess(false);
        baseResponse.setFailed(ErrorCodeDefs.NOT_FOUND, ErrorCodeDefs.getErrMsg(ErrorCodeDefs.NOT_FOUND));
        return ResponseEntity.ok(baseResponse);
    }
}
