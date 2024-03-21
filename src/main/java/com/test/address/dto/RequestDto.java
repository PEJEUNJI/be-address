package com.test.address.dto;

// RequestDto 클래스
public class RequestDto implements BaseDto {
    private int pageNumber;
    private int pageSize;
    private String orderFg;

    public RequestDto(int pageNumber, int pageSize, String orderFg) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.orderFg = orderFg;
    }

    // 기본 생성자
    public RequestDto() {
        this.pageNumber = 0;
        this.pageSize = 10;
        this.orderFg = "ASC";
    }


    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public int getPageSize() {
        return pageSize;
    }

    @Override
    public String getOrderFg() {
        return orderFg;
    }

    @Override
    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    @Override
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public void setOrderFg(String orderFg) {
        this.orderFg = orderFg;
    }
}