package com.test.address.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.address.dto.AddressBookDto;
import com.test.address.dto.RequestDto;
import com.test.address.dto.ResponseDto;
import com.test.address.service.AddressBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;

import java.util.*;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AddressBookController.class)
public class AddressBookControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressBookService addressBookService;

    @Test
    public void testDeleteByNameLike() throws Exception {
        // 테스트에 사용할 데이터 생성
        String telephone = "1234567890";
        AddressBookDto originalDto = new AddressBookDto(telephone,
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        // 서비스 메서드 호출 시 반환할 값 설정
        when(addressBookService.deleteByNameLike(any(RequestDto.class), anyString()))
                .thenReturn(Collections.singletonList(originalDto));

        // 테스트 수행
        mockMvc.perform(delete("/addressbook/deleteByNameLike")
                        .param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("데이터 삭제가 성공적으로 완료되었습니다."));
    }

    @Test
    public void testSearchByNameLike() throws Exception {
        // 테스트에 사용할 데이터 생성
        String name = "테스트";
        List<AddressBookDto> searchResult = new ArrayList<>();
        AddressBookDto addressBookDto1 = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");
        AddressBookDto addressBookDto2 = new AddressBookDto("01022221111",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");
        searchResult.add(addressBookDto2);
        searchResult.add(addressBookDto1);

        // 서비스 메서드 호출 시 반환할 값 설정
        when(addressBookService.searchByNameLike(any(RequestDto.class), anyString()))
                .thenReturn(searchResult);

        // RequestDto 객체 생성
        RequestDto requestDto = new RequestDto(0, 20, "DESC");

        // 테스트 수행
        mockMvc.perform(get("/addressbook/searchByNameLike")
                        .param("name", name)
                        .param("orderFg", requestDto.getOrderFg()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 이름으로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].telephone").value(addressBookDto2.getTelephone()))
                .andExpect(jsonPath("$.data[0].email").value(addressBookDto2.getEmail()))
                .andExpect(jsonPath("$.data[0].address").value(addressBookDto2.getAddress()))
                .andExpect(jsonPath("$.data[0].name").value(addressBookDto2.getName()));
    }
    @Test
    public void testSearchByPhoneNumber() throws Exception {
        // 테스트에 사용할 전화번호
        String telephone = "01011112222";

        // 주소록 조회 결과 생성
        AddressBookDto addressBookDto = new AddressBookDto(telephone,
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");

        Optional<AddressBookDto> result = Optional.of(addressBookDto);

        // 서비스 메서드 호출 시 반환할 값 설정
        when(addressBookService.searchByTelephone(telephone)).thenReturn(result);

        // 테스트 수행 및 검증
        mockMvc.perform(get("/addressbook/searchByPhoneNumber")
                        .param("telephone", telephone))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 전화번호로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.telephone").value(addressBookDto.getTelephone()))
                .andExpect(jsonPath("$.data.email").value(addressBookDto.getEmail()))
                .andExpect(jsonPath("$.data.address").value(addressBookDto.getAddress()))
                .andExpect(jsonPath("$.data.name").value(addressBookDto.getName()));
    }
    @Test
    public void testSearchByAddressLike() throws Exception {
        // 테스트에 사용할 주소
        String address = "전라북도 남원시";

        // 주소록 조회 결과 생성
        List<AddressBookDto> searchResult = new ArrayList<>();
        AddressBookDto addressBookDto1 = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");
        AddressBookDto addressBookDto2 = new AddressBookDto("01022221111",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");
        searchResult.add(addressBookDto2);
        searchResult.add(addressBookDto1);

        // 서비스 메서드 호출 시 반환할 값 설정
        when(addressBookService.searchByAddressLike(any(RequestDto.class), anyString()))
                .thenReturn(searchResult);

        // RequestDto 객체 생성
        RequestDto requestDto = new RequestDto(0, 10, "DESC");

        // 테스트 수행 및 검증
        mockMvc.perform(get("/addressbook/searchByAddressLike")
                        .param("address", address)
                        .param("pageNumber", String.valueOf(requestDto.getPageNumber()))
                        .param("pageSize", String.valueOf(requestDto.getPageSize()))
                        .param("orderFg", requestDto.getOrderFg()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 주소로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].telephone").value(addressBookDto2.getTelephone()))
                .andExpect(jsonPath("$.data[0].email").value(addressBookDto2.getEmail()))
                .andExpect(jsonPath("$.data[0].address").value(addressBookDto2.getAddress()))
                .andExpect(jsonPath("$.data[0].name").value(addressBookDto2.getName()))
                .andExpect(jsonPath("$.data[1].telephone").value(addressBookDto1.getTelephone()))
                .andExpect(jsonPath("$.data[1].email").value(addressBookDto1.getEmail()))
                .andExpect(jsonPath("$.data[1].address").value(addressBookDto1.getAddress()))
                .andExpect(jsonPath("$.data[1].name").value(addressBookDto1.getName()));
    }

    @Test
    public void testDeleteByTelephone() throws Exception {
        // 삭제할 전화번호
        String telephone = "01011112222";

        // 삭제된 항목이 없는 경우의 서비스 메서드 반환값 설정
        when(addressBookService.deleteByTelephone(telephone))
                .thenReturn(Optional.empty());

        // 테스트 수행
        mockMvc.perform(delete("/addressbook/deleteByTelephone")
                        .param("telephone", telephone))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("삭제할 데이터가 없습니다."))
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.data").doesNotExist());

        // 삭제된 항목이 있는 경우의 서비스 메서드 반환값 설정
        AddressBookDto deletedEntry = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");

        when(addressBookService.deleteByTelephone(telephone))
                .thenReturn(Optional.of(deletedEntry));

        // 테스트 수행
        mockMvc.perform(delete("/addressbook/deleteByTelephone")
                        .param("telephone", telephone))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("데이터 삭제가 성공적으로 완료되었습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.telephone").value(deletedEntry.getTelephone()))
                .andExpect(jsonPath("$.data.email").value(deletedEntry.getEmail()))
                .andExpect(jsonPath("$.data.address").value(deletedEntry.getAddress()))
                .andExpect(jsonPath("$.data.name").value(deletedEntry.getName()));
    }

    @Test
    public void testUpdateByTelephone() throws Exception {
        // 업데이트할 전화번호와 주소록 정보
        String telephone = "01012345678";
        AddressBookDto addressBookDto = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");

        AddressBookDto previousAddressBookDto = new AddressBookDto("01011112222",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        // 유효성 검증 실패 메세지 설정
        String errorMessage = "주소록 정보가 유효하지 않습니다.";
        BindingResult bindingResult = new BeanPropertyBindingResult(addressBookDto, "addressBookDto");
        bindingResult.reject("globalError", errorMessage);

        // 유효성 검증 실패 경우의 서비스 메서드 반환값 설정
        when(addressBookService.updateByTelephone(eq(telephone), any(AddressBookDto.class)))
                .thenReturn(new ResponseDto(errorMessage, false, null));

        // 테스트 수행
        mockMvc.perform(put("/addressbook/updateByTelephone")
                        .param("telephone", telephone)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(addressBookDto)))
                .andExpect(status().isNoContent())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("변경할 데이터가 없습니다."))
                .andExpect(jsonPath("$.success").value(false));

        // 업데이트 성공 경우의 서비스 메서드 반환값 설정
        Map<String, AddressBookDto> updatedData = new HashMap<>();
        updatedData.put("previousData", previousAddressBookDto);
        updatedData.put("updatedData", addressBookDto);
        when(addressBookService.updateByTelephone(eq(telephone), any(AddressBookDto.class)))
                .thenReturn(new ResponseDto<>("변경 완료 되었습니다.", true, updatedData));

        // 테스트 수행
        mockMvc.perform(put("/addressbook/updateByTelephone")
                        .param("telephone", telephone)
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
    }
    @Test
    public void testSearchByEmail() throws Exception {
        // 테스트에 사용할 이메일
        String email = "test2@example.com";
        AddressBookDto addressBookDto = new AddressBookDto("01011112222",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        // 서비스 메서드 호출 시 반환할 값 설정
        when(addressBookService.searchByEmail(email)).thenReturn(Optional.of(addressBookDto));

        // 테스트 수행 및 검증
        mockMvc.perform(get("/addressbook/searchByEmail")
                        .param("email", email))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("주어진 email로 데이터를 조회하였습니다."))
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.telephone").value(addressBookDto.getTelephone()))
                .andExpect(jsonPath("$.data.email").value(addressBookDto.getEmail()))
                .andExpect(jsonPath("$.data.address").value(addressBookDto.getAddress()))
                .andExpect(jsonPath("$.data.name").value(addressBookDto.getName()));
    }

}
