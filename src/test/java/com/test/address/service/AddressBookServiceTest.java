package com.test.address.service;
import com.test.address.dto.AddressBookDto;
import com.test.address.dto.RequestDto;
import com.test.address.dto.ResponseDto;
import com.test.address.repository.MemoryAddressBookRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
public class AddressBookServiceTest {
    @Mock
    private MemoryAddressBookRepository addressBookRepositoryMock;

    @InjectMocks
    private AddressBookService addressBookService;

    public AddressBookServiceTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testDeleteByNameLike() {
        // given
        String name = "test";
        List<AddressBookDto> deletedEntries = new ArrayList<>();
        //given
        AddressBookDto addressBook1 = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시",
                "테스트1");

        AddressBookDto addressBook2 = new AddressBookDto("01022222222",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        deletedEntries.add(addressBook1);
        deletedEntries.add(addressBook2);
        // when
        when(addressBookRepositoryMock.deleteByNameLike(anyString())).thenReturn(deletedEntries);
        List<AddressBookDto> result = addressBookService.deleteByNameLike( new RequestDto(),name);

        // then
        verify(addressBookRepositoryMock, times(1)).deleteByNameLike(name); // deleteByNameLike 메서드가 호출되었는지 확인
        assertThat(result.size()).isEqualTo(2); // 삭제된 엔트리 개수 확인
    }

    @Test
    void testSearchByNameLike() {
        // given
        String name = "테스트";
        RequestDto requestDtoAsc = new RequestDto();
        RequestDto requestDtoDesc = new RequestDto(0,10,"DESC");

        List<AddressBookDto> resultListAsc = new ArrayList<>();
        List<AddressBookDto> resultListDesc = new ArrayList<>();
        //given
        AddressBookDto addressBook1 = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시",
                "테스트1");

        AddressBookDto addressBook2 = new AddressBookDto("01022222222",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        resultListAsc.add(addressBook1);
        resultListAsc.add(addressBook2);
        resultListDesc.add(addressBook2);
        resultListDesc.add(addressBook1);

        // when
        when(addressBookRepositoryMock.findByNameContainingOrderByNameAsc(anyString())).thenReturn(resultListAsc);
        when(addressBookRepositoryMock.findByNameContainingOrderByNameDesc(anyString())).thenReturn(resultListDesc);

        // then
        List<AddressBookDto> resultAsc = addressBookService.searchByNameLike(requestDtoAsc,name);
        Assertions.assertThat(resultAsc).isEqualTo(resultListAsc); // 이름 오름차순으로 검색 결과 확인

        List<AddressBookDto> resultDesc = addressBookService.searchByNameLike(requestDtoDesc,name);
        Assertions.assertThat(resultDesc).isEqualTo(resultListDesc); // 이름 내림차순으로 검색 결과 확인
    }

    @Test
    void testSearchByTelephone() {
        // given
        String telephone = "01011112222";
        AddressBookDto addressBook1 = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시",
                "테스트1");
        Optional<AddressBookDto> expectedResult = Optional.of(addressBook1);

        // when
        when(addressBookRepositoryMock.findByTelephone(anyString())).thenReturn(expectedResult);

        // then
        Optional<AddressBookDto> result = addressBookService.searchByTelephone(telephone);
        Assertions.assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testSearchByAddressLike() {
        // given
        String address = "전라북도";

        RequestDto requestDtoAsc = new RequestDto();
        RequestDto requestDtoDesc = new RequestDto(0,10,"DESC");

        List<AddressBookDto> resultListAsc = new ArrayList<>();
        List<AddressBookDto> resultListDesc = new ArrayList<>();

        AddressBookDto addressBook1 = new AddressBookDto("01011112222",
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");

        AddressBookDto addressBook2 = new AddressBookDto("01022222222",
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        resultListAsc.add(addressBook1);
        resultListAsc.add(addressBook2);

        resultListDesc.add(addressBook2);
        resultListDesc.add(addressBook1);

        // when
        when(addressBookRepositoryMock.findByAddressContainingOrderByAddressAsc(address)).thenReturn(resultListAsc);
        when(addressBookRepositoryMock.findByAddressContainingOrderByAddressDesc(address)).thenReturn(resultListDesc);

        // then
        List<AddressBookDto> resultAsc = addressBookService.searchByAddressLike(requestDtoAsc,address);
        assertThat(resultAsc).containsExactlyElementsOf(resultListAsc); // 주소기준 오름차순으로 검색 결과 확인

        List<AddressBookDto> resultDesc = addressBookService.searchByAddressLike(requestDtoDesc,address);
        assertThat(resultDesc).containsExactlyElementsOf(resultListDesc); // 주소기준 내름차순으로 검색 결과 확인
    }

    @Test
    void testDeleteByTelephone() {
        // given
        String telephone = "01011112222";
        AddressBookDto addressBookToDelete = new AddressBookDto(telephone,
                "test1@test.com",
                "전라북도 남원시",
                "테스트1");

        // when
        when(addressBookRepositoryMock.deleteByTelephone(telephone)).thenReturn(Optional.of(addressBookToDelete));
        Optional<AddressBookDto> result = addressBookService.deleteByTelephone(telephone);

        // then
        verify(addressBookRepositoryMock, times(1)).deleteByTelephone(telephone); // deleteByTelephone 메서드가 호출되었는지 확인
        assertThat(result.isPresent()).isTrue(); // 결과값이 존재하는지 확인
        assertThat(result.get()).isEqualTo(addressBookToDelete); // 반환된 값이 예상한 값과 일치하는지 확인
    }

    @Test
    public void testUpdateByTelephone_Success() {
        // 테스트 데이터 설정
        String telephone = "01011112222";

        AddressBookDto updatedDto = new AddressBookDto(telephone,
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");

        AddressBookDto originalDto = new AddressBookDto(telephone,
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        // Mock 주소록 레포지토리의 동작 설정
        // Mock 주소록 레포지토리의 동작 설정
        when(addressBookRepositoryMock.findByTelephone(telephone)).thenReturn(Optional.of(originalDto));
        when(addressBookRepositoryMock.save(updatedDto)).thenReturn(updatedDto);

        // 테스트 수행
        ResponseDto responseDto = addressBookService.updateByTelephone(telephone, updatedDto);

        // 결과 검증
        assertTrue(responseDto.isSuccess());
        assertEquals("업데이트가 성공적으로 수행되었습니다.", responseDto.getMessage());
        // 업데이트 이전 데이터와 업데이트된 데이터를 함께 반환하므로, 업데이트 이전 데이터와 업데이트된 데이터 모두 검증
        Map<String, AddressBookDto> dataMap = (Map<String, AddressBookDto>) responseDto.getData();
        assertEquals(originalDto, dataMap.get("previousData"));
        assertEquals(updatedDto, dataMap.get("updatedData"));
    }

    @Test
    public void testUpdateByTelephone_NotFound() {
        // 테스트 데이터 설정
        String telephone = "01011112222";
        AddressBookDto updatedDto = new AddressBookDto(telephone,
                "test1@test.com",
                "전라북도 남원시1",
                "테스트1");

        // Mock 주소록 레포지토리의 동작 설정 (업데이트할 주소록이 존재하지 않는 경우)
        when(addressBookRepositoryMock.findByTelephone(telephone)).thenReturn(Optional.empty());
        when(addressBookRepositoryMock.save(updatedDto)).thenReturn(updatedDto);

        // 테스트 수행
        ResponseDto responseDto = addressBookService.updateByTelephone(telephone, updatedDto);

        // 결과 검증
        assertFalse(responseDto.isSuccess()); // 업데이트에 실패했으므로 isSuccess()는 false여야 함
        assertEquals("주어진 전화번호에 해당하는 주소록이 존재하지 않습니다.", responseDto.getMessage()); // 메시지 확인
        assertNull(responseDto.getData());
    }

    @Test
    public void testUpdateByTelephone_NotValid() {
        // 테스트 데이터 설정
        String telephone = "01011112222";

        AddressBookDto updatedDto = new AddressBookDto(telephone,
                "test1",
                "전라북도 남원시1",
                "테스트1");

        AddressBookDto originalDto = new AddressBookDto(telephone,
                "test2@test.com",
                "전라북도 남원시2",
                "테스트2");

        // Mock 주소록 레포지토리의 동작 설정
        when(addressBookRepositoryMock.findByTelephone(telephone)).thenReturn(Optional.of(originalDto));
        when(addressBookRepositoryMock.save(updatedDto)).thenReturn(updatedDto);

        // 테스트 수행
        ResponseDto responseDto = addressBookService.updateByTelephone(telephone, updatedDto);

        // 결과 검증
        assertFalse(responseDto.isSuccess());
        assertEquals("유효하지 않은 email 주소 입니다.", responseDto.getMessage());
    }

    @Test
    void testSearchByEmail() {
        // 테스트 데이터 설정
        String emailToSearch = "test1@test.com";
        AddressBookDto addressBook1 = new AddressBookDto("01011112222",
                emailToSearch,
                "전라북도 남원시",
                "테스트1");

        // Mock 주소록 레포지토리의 동작 설정
        when(addressBookRepositoryMock.findByEmail(emailToSearch)).thenReturn(Optional.of(addressBook1));

        // 테스트 수행
        Optional<AddressBookDto> result = addressBookService.searchByEmail(emailToSearch);

        // 결과 검증
        assertEquals(addressBook1, result.get());
    }
}
