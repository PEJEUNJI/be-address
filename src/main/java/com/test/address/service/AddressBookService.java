package com.test.address.service;

import com.test.address.dto.AddressBookDto;
import com.test.address.dto.RequestDto;
import com.test.address.dto.ResponseDto;
import com.test.address.repository.MemoryAddressBookRepository;
import com.test.address.utils.ValidationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AddressBookService {
    private final MemoryAddressBookRepository addressBookRepository;
    private static final Log log = LogFactory.getLog(MemoryAddressBookRepository.class);


    @Autowired
    public AddressBookService(MemoryAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }


    /**
     * 이름을 라이크 검색하여 해당 이름의 주소록을 모두 삭제
     * @param name
     * @return
     */
    public List<AddressBookDto> deleteByNameLike(RequestDto requestDto, String name) {
        List<AddressBookDto> allResults = addressBookRepository.deleteByNameLike(name);
        // 페이징을 적용하여 일부 결과만 반환
        return getPage(allResults, requestDto.getPageNumber(), requestDto.getPageSize());
    }


    /**
     * 이름을 라이크 검색하여 해당 주소록 리스트를 조회
     * @param requestDto
     * @param name
     * @return
     */
    public List<AddressBookDto> searchByNameLike(RequestDto requestDto, String name) {
        List<AddressBookDto> allResults;
        if (requestDto.getOrderFg().equals("DESC")) {
            allResults = addressBookRepository.findByNameContainingOrderByNameDesc(name);
        } else {
            allResults = addressBookRepository.findByNameContainingOrderByNameAsc(name);
        }
        // 페이징을 적용하여 일부 결과만 반환
        return getPage(allResults, requestDto.getPageNumber(), requestDto.getPageSize());
    }

    /**
     * 폰넘버를 검색하여 해당 주소록 리스트를 조회
     * @param telephone
     * @return
     */
    public Optional<AddressBookDto> searchByTelephone(String telephone) {
        return addressBookRepository.findByTelephone(telephone);
    }

    /**
     * 주소를 라이크 검색하여 해당 주소록 리스트를 조회
     * @param requestDto
     * @param address
     * @return
     */
    public List<AddressBookDto> searchByAddressLike(RequestDto requestDto, String address) {
        List<AddressBookDto> allResults;

        // 전체 결과를 가져옴
        if (requestDto.getOrderFg().equals("DESC")) {
            allResults = addressBookRepository.findByAddressContainingOrderByAddressDesc(address);
        } else {
            allResults = addressBookRepository.findByAddressContainingOrderByAddressAsc(address);
        }
        // 페이징을 적용하여 일부 결과만 반환
        return getPage(allResults, requestDto.getPageNumber(), requestDto.getPageSize());
    }

    private List<AddressBookDto> getPage(List<AddressBookDto> allResults, int pageNumber, int pageSize) {
        int startIndex = pageNumber * pageSize;
        int endIndex = Math.min(startIndex + pageSize, allResults.size());
        return allResults.subList(startIndex, endIndex);
    }

    /**
     * 폰넘버를 받아 해당 내용의 데이터를 주소록에서 모두 삭제
     * @param telephone
     */
    public Optional<AddressBookDto> deleteByTelephone(String telephone) {
        return addressBookRepository.deleteByTelephone(telephone);
    }

    /**
     * 폰넘버를 받아 전달받은 주소록 데이터를 주소록에 변경 반영
     *
     * @param telephone
     * @param updatedDto
     */
    public ResponseDto updateByTelephone(String telephone, AddressBookDto updatedDto) {
        // 이전 데이터 저장
        Optional<AddressBookDto> previousData = addressBookRepository.findByTelephone(telephone);
        if (!ValidationUtil.isValidEmail(updatedDto.getEmail())) {
            return new ResponseDto("유효하지 않은 email 주소 입니다.", false, null);
        }
        if (previousData.isPresent() && previousData.get().getTelephone().equals(updatedDto.getTelephone())) {
            // 맵에서 해당 전화번호를 가진 엔트리를 새로운 주소록 엔트리로 업데이트, 전화번호는 업데이트 하지 않는다.
            addressBookRepository.save(updatedDto);
            // 업데이트 전 후 데이터를 함께 저장
            Map<String, AddressBookDto> dataMap = new HashMap<>();
            dataMap.put("previousData", previousData.get());
            dataMap.put("updatedData", updatedDto);
            return new ResponseDto("업데이트가 성공적으로 수행되었습니다.", true, dataMap);
        } else {
            // 1. 기존에 해당 전화번호와 같은 경우가 없는 경우
            // 2. 기존 dto의 전화번호와 주어진 전화번호가 일치하지 않는 경우
            return new ResponseDto("주어진 전화번호에 해당하는 주소록이 존재하지 않습니다.", false, null);
        }
    }

    /**
     * email로 검색하여 해당 주소록을 조회-중복 이메일은 없으므로 단일 객체로 리턴
     * @param email
     * @return
     */
    public Optional<AddressBookDto> searchByEmail(String email) {
        return addressBookRepository.findByEmail(email);
    }

    /**
     * 이메일 중복 검사
     * @param email
     * @return
     */
    public boolean isDuplicateEmail(String email) {

        return addressBookRepository.findByEmail(email).isPresent();
    }

    /**
     * 주소록 등록
     *
     * @param addressBookDto
     */
    public void saveAddressBookEntry(AddressBookDto addressBookDto) {
        //중복 전화번호 검증
        if(checkDuplicateTelephone(addressBookDto.getTelephone())){
            log.error("이미 존재하는 전화번호 입니다." + addressBookDto.getTelephone());
            return;
        }
        if (!ValidationUtil.isValidPhoneNumber(addressBookDto.getTelephone())) {
            log.error("형식을 만족하지 못하는 데이터 :" + addressBookDto.getTelephone());
            return;
        }
        if (!ValidationUtil.isValidEmail(addressBookDto.getEmail())) {
            log.error("형식을 만족하지 못하는 데이터 :" + addressBookDto.getEmail());
            return;
        }
        if (isDuplicateEmail(addressBookDto.getEmail())) {
            log.error("이미 존재하는 email 입니다." + addressBookDto.getEmail());
            return;
        }
        addressBookRepository.save(addressBookDto);
    }

    private boolean checkDuplicateTelephone(String telephone) {
        return addressBookRepository.findByTelephone(telephone).isPresent();
    }

    public List<AddressBookDto> findAll() {
        return addressBookRepository.findAll();
    }

    public void clearAddressBook() {
        addressBookRepository.clearAddressBook();
    }
}
