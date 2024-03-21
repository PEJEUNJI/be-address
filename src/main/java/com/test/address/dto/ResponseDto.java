package com.test.address.dto;

import org.springframework.validation.ObjectError;

import java.util.List;

public class ResponseDto<T> {
    private String message;
    private List<ObjectError> errors;
    private boolean success;
    private int totalPages;
    private long totalElements;
    private T data;
/**
    // 실패 시에 사용되는 생성자
    public ResponseDto(String message, List<ObjectError> errors, boolean success) {
        this.message = message;
        this.errors = errors;
        this.success = success;
    }
**/
    // 성공 시에 데이터를 포함하는 생성자
    public ResponseDto(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<ObjectError> getErrors() {
        return errors;
    }

    public void setErrors(List<ObjectError> errors) {
        this.errors = errors;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}