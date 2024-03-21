package com.test.address.service;

import com.test.address.dto.AddressBookDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class AddressBookLoaderTest {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private AddressBookLoader addressBookLoader;

    @BeforeEach
    public void beforeEach(){
        addressBookService.clearAddressBook();
    }


    @Test
    public void testLoadFromFile()  {
        //when
        addressBookLoader.loadFromFile("test_success_address.csv");
        //then
        // 예상되는 엔티티 개수
        int expectedEntityCount = 5;
        // 예상되는 데이터를 디비에서 불러와 검증
        AddressBookDto addressBook = new AddressBookDto("", "", "", "");
        //Pageable pageable = PageRequest.of(0, 10);
        // 검증을 위한 모든 데이터 조회
        List<AddressBookDto> addressBookList = addressBookService.findAll();
        assertEquals(expectedEntityCount, addressBookList.size());
        // 예상되는 특정 데이터를 검증
        AddressBookDto addressBookDto = addressBookService.searchByTelephone("010-000-0004").get();
        assertEquals("lee3@test.com", addressBookDto.getEmail());
        assertEquals("이몽룡", addressBookDto.getName());

    }

}