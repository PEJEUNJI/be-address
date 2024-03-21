package com.test.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.address.dto.AddressBookDto;
import com.test.address.dto.RequestDto;
import com.test.address.service.AddressBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressBookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AddressBookService addressBookService;

    @Test
    public void testDeleteByNameLike() throws Exception {
        // 테스트에 사용할 데이터 전라북도 남원시,010-000-0004,lee3@test.com,이몽룡
        AddressBookDto result = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        AddressBookDto result2 = new AddressBookDto("010-000-0004",
                "lee3@test.com",
                "전라북도 남원시",
                "이몽룡");

        // 테스트 수행
        mockMvc.perform(delete("/addressbook/deleteByNameLike")
                        .param("name", result.getName()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("데이터 삭제가 성공적으로 완료되었습니다."));
        // 테스트 데이터 원복
        addressBookService.saveAddressBookEntry(result);
        addressBookService.saveAddressBookEntry(result2);
    }
    @Test
    public void testSearchByNameLike() throws Exception {
        // 테스트에 사용할 데이터 생성
        AddressBookDto result = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");
        // RequestDto 객체 생성
        RequestDto requestDto = new RequestDto(0, 10, "DESC");

        // 테스트 수행
        mockMvc.perform(get("/addressbook/searchByNameLike")
                        .param("name", result.getName())
                        .param("orderFg", requestDto.getOrderFg()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 이름으로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].telephone").value(result.getTelephone()))
                .andExpect(jsonPath("$.data[0].email").value(result.getEmail()))
                .andExpect(jsonPath("$.data[0].address").value(result.getAddress()))
                .andExpect(jsonPath("$.data[0].name").value(result.getName()));
    }
    @Test
    public void testSearchByPhoneNumber() throws Exception {

        // 테스트에 사용할 데이터 생성
        AddressBookDto result = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        // 테스트 수행 및 검증
        mockMvc.perform(get("/addressbook/searchByPhoneNumber")
                        .param("telephone", result.getTelephone()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 전화번호로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.telephone").value(result.getTelephone()))
                .andExpect(jsonPath("$.data.email").value(result.getEmail()))
                .andExpect(jsonPath("$.data.address").value(result.getAddress()))
                .andExpect(jsonPath("$.data.name").value(result.getName()));
    }
    @Test
    public void testSearchByAddressLike() throws Exception {
        // 테스트에 사용할 데이터 생성
        AddressBookDto result = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        AddressBookDto testData = new AddressBookDto("0100000002",
                "lee222@test.com",
                "경기도 성남시2",
                "이몽룡2");
        addressBookService.saveAddressBookEntry(testData);

        // RequestDto 객체 생성
        RequestDto requestDto = new RequestDto(0, 10, "DESC");

        // 테스트 수행 및 검증
        mockMvc.perform(get("/addressbook/searchByAddressLike")
                        .param("address", result.getAddress())
                        .param("pageNumber", String.valueOf(requestDto.getPageNumber()))
                        .param("pageSize", String.valueOf(requestDto.getPageSize()))
                        .param("orderFg", requestDto.getOrderFg()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 주소로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].telephone").value(testData.getTelephone()))
                .andExpect(jsonPath("$.data[0].email").value(testData.getEmail()))
                .andExpect(jsonPath("$.data[0].address").value(testData.getAddress()))
                .andExpect(jsonPath("$.data[0].name").value(testData.getName()))
                .andExpect(jsonPath("$.data[1].telephone").value(result.getTelephone()))
                .andExpect(jsonPath("$.data[1].email").value(result.getEmail()))
                .andExpect(jsonPath("$.data[1].address").value(result.getAddress()))
                .andExpect(jsonPath("$.data[1].name").value(result.getName()));
        //테스트 데이터 삭제

        addressBookService.deleteByTelephone(testData.getTelephone());
    }
    @Test
    public void testDeleteByTelephone() throws Exception {
        // 테스트에 사용할 데이터 생성
        AddressBookDto result = new AddressBookDto("0100000002",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        // 테스트 수행
        mockMvc.perform(delete("/addressbook/deleteByTelephone")
                        .param("telephone", result.getTelephone()))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("삭제할 데이터가 없습니다."))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist());

        // 삭제된 항목이 있는 경우의 서비스 메서드 반환값 설정
        AddressBookDto deletedEntry = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        // 테스트 수행
        mockMvc.perform(delete("/addressbook/deleteByTelephone")
                        .param("telephone", deletedEntry.getTelephone()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("데이터 삭제가 성공적으로 완료되었습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.telephone").value(deletedEntry.getTelephone()))
                .andExpect(jsonPath("$.data.email").value(deletedEntry.getEmail()))
                .andExpect(jsonPath("$.data.address").value(deletedEntry.getAddress()))
                .andExpect(jsonPath("$.data.name").value(deletedEntry.getName()));

        // 테스트 데이터 원복
        addressBookService.saveAddressBookEntry(deletedEntry);
    }
    @Test
    public void testUpdateByTelephone() throws Exception {
        // 테스트에 사용할 데이터 생성
        AddressBookDto previousAddressBookDto = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        AddressBookDto addressBookDto = new AddressBookDto("0100000002",
                "lee2@test.com",
                "경기도 성남시2",
                "이몽룡2");

        // 테스트 수행
        mockMvc.perform(put("/addressbook/updateByTelephone")
                        .param("telephone", previousAddressBookDto.getTelephone())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressBookDto)))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(false));

        addressBookDto = new AddressBookDto(previousAddressBookDto.getTelephone(),
                "lee2@test.com",
                "경기도 성남시2",
                "이몽룡2");
        // 테스트 수행
        mockMvc.perform(put("/addressbook/updateByTelephone")
                        .param("telephone", previousAddressBookDto.getTelephone())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressBookDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("변경 완료 되었습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.previousData.telephone").value(previousAddressBookDto.getTelephone()))
                .andExpect(jsonPath("$.data.previousData.email").value(previousAddressBookDto.getEmail()))
                .andExpect(jsonPath("$.data.previousData.address").value(previousAddressBookDto.getAddress()))
                .andExpect(jsonPath("$.data.previousData.name").value(previousAddressBookDto.getName()))
                .andExpect(jsonPath("$.data.updatedData.telephone").value(addressBookDto.getTelephone()))
                .andExpect(jsonPath("$.data.updatedData.email").value(addressBookDto.getEmail()))
                .andExpect(jsonPath("$.data.updatedData.address").value(addressBookDto.getAddress()))
                .andExpect(jsonPath("$.data.updatedData.name").value(addressBookDto.getName()));
        //data 원복
        addressBookService.updateByTelephone(previousAddressBookDto.getTelephone(),previousAddressBookDto);
    }
    @Test
    public void testSearchByEmail() throws Exception {
        // 테스트에 사용할 이메일
        // 테스트에 사용할 데이터 생성
        AddressBookDto result = new AddressBookDto("0100000001",
                "lee@test.com",
                "경기도 성남시",
                "이몽룡");

        // 테스트 수행 및 검증
        mockMvc.perform(get("/addressbook/searchByEmail")
                        .param("email", result.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 email로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.telephone").value(result.getTelephone()))
                .andExpect(jsonPath("$.data.email").value(result.getEmail()))
                .andExpect(jsonPath("$.data.address").value(result.getAddress()))
                .andExpect(jsonPath("$.data.name").value(result.getName()));
    }

}
