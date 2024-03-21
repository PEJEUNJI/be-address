package com.test.address.dto;


public interface BaseDto {
    int getPageNumber();
    int getPageSize();
    String getOrderFg();
    void setPageNumber(int pageNumber);
    void setPageSize(int pageSize);
    void setOrderFg(String orderFg);
}