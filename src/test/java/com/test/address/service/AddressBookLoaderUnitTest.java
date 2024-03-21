package com.test.address.service;

import com.test.address.dto.AddressBookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AddressBookLoaderUnitTest {

    @Mock
    private AddressBookService addressBookServiceMock;

    @InjectMocks
    private AddressBookLoader addressBookLoader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testLoadFromFile() {
        // 준비
        String filePath = "unit_test_success_address.csv";
        List<String> lines = new ArrayList<>();
        lines.add("서울시 광진구,01000000000,hong@test.com,홍길동");
        lines.add("서울시 강남구,01011111111,kim@test.com,김철수");
        lines.add("서울시 강남구,01011111111,kim,김철수");

        // Mock 설정
        when(addressBookServiceMock.isDuplicateEmail(anyString())).thenReturn(false);

        // 테스트
        addressBookLoader.loadFromFile(filePath);

        // 검증
        verify(addressBookServiceMock, times(2)).saveAddressBookEntry(any(AddressBookDto.class));
    }

}
