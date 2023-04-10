package com.example.democrm.exception;

import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.response.BaseResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler {
    @ResponseStatus(OK)
    @ResponseBody
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public BaseResponse methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        BaseResponse response = BaseResponse.builder()
                .success(false)
                .error(processFieldErrors(fieldErrors))
                .build();
        return response;
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public BaseResponse notReadableExceptionHandle(HttpMessageNotReadableException ex) {
        return BaseResponse.builder().success(false).error(
                        BaseResponse.Error.builder()
                                .code(ErrorCodeDefs.VALIDATION_ERROR)
                                .message(ErrorCodeDefs.getErrMsg(ErrorCodeDefs.VALIDATION_ERROR))
                                .build()
                )
                .build();
    }

    @ResponseStatus(OK)
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public BaseResponse methodArgumentNotValidException(Exception ex) {
        BaseResponse response = new BaseResponse();
        response.setFailed(ErrorCodeDefs.SERVER_ERROR, ex.getMessage());
        return response;
    }

    public BaseResponse.Error processFieldErrors(List<FieldError> fieldErrors) {
        BaseResponse.Error error = BaseResponse.Error.builder()
                .code(ErrorCodeDefs.VALIDATION_ERROR)
                .message(ErrorCodeDefs.getErrMsg(ErrorCodeDefs.VALIDATION_ERROR))
                .build();
        List<BaseResponse.ErrorDetail> errorDetailList = new ArrayList<>();
        for (FieldError fieldError : fieldErrors) {
            BaseResponse.ErrorDetail errorDetail = BaseResponse.ErrorDetail.builder()
                    .id(fieldError.getField())
                    .message(fieldError.getDefaultMessage())
                    .build();
            errorDetailList.add(errorDetail);
        }
        error.setErrorDetailList(errorDetailList);
        return error;
    }


}
