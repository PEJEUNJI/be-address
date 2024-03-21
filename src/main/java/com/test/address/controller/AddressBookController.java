package com.test.address.controller;

import com.test.address.dto.AddressBookDto;
import com.test.address.dto.RequestDto;
import com.test.address.dto.ResponseDto;
import com.test.address.service.AddressBookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

/**
 * 1. 이름을 라이크 검색해서 해당 이름의 주소록을 모두 삭제(삭제)
 * 2. 이름을 라이크 검색해서 해당 주소록 리스트를 조회한다(조회)
 * 3. 폰넘버를 검색해서 해당 주소록 리스트를 조회한다.(조회)
 * 4. 주소를 라이크 검색해서 해당 주소록 리스트를 조회한다.(조회)
 * 5. 폰넘버를 받아 해당 내용의 데이터를 주소록에서 모두 삭제한다.(삭제)
 * 6. 폰넘버를 받아 전달받은 주소록 데이터를 주소록에 변경 반영한다.(업데이트)
 *
 * 주의) 삭제의 경우에는 삭제 후 삭제 된 주소로 데이터를 리턴해준다.
 * 주의) 업데이트의 경우에는 업데이트 전 데이터와 업데이트 후 데이터를 구분해서 리턴해준다.
 */
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import java.util.List;

/**
 * 1. 이름을 라이크 검색해서 해당 이름의 주소록을 모두 삭제(삭제)
 * 2. 이름을 라이크 검색해서 해당 주소록 리스트를 조회한다(조회)
 * 3. 폰넘버를 검색해서 해당 주소록 리스트를 조회한다.(조회)
 * 4. 주소를 라이크 검색해서 해당 주소록 리스트를 조회한다.(조회)
 * 5. 폰넘버를 받아 해당 내용의 데이터를 주소록에서 모두 삭제한다.(삭제)
 * 6. 폰넘버를 받아 전달받은 주소록 데이터를 주소록에 변경 반영한다.(업데이트)
 * <p>
 * 주의) 삭제의 경우에는 삭제 후 삭제 된 주소로 데이터를 리턴해준다.
 * 주의) 업데이트의 경우에는 업데이트 전 데이터와 업데이트 후 데이터를 구분해서 리턴해준다.
 * ************************ ReadMe ********************************
 * 조회 파라메터
 * 필터: 전화번호, 이메일, 주소, 이름
 * 정렬: (전화번호 | 이메일 | 주소 | 이름), (오름차순 | 내림차순)
 * 응답: 고객 정보 목록 (josn)
 * 수정 파라메터: 전화번호
 * 응답: 고객 정보 (json)
 * 삭제 파라메터: 전화번호
 * 응답: 삭제된 고객 정보 (json)
 */
@RestController
@RequestMapping("/addressbook")
public class AddressBookController {

    private final AddressBookService addressBookService;

    @Autowired
    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    /**
     * 1.이름을 라이크 검색하여 해당 이름의 주소록을 모두 삭제
     *
     * @param name
     */
    @DeleteMapping("/deleteByNameLike")
    public ResponseEntity<ResponseDto<List<AddressBookDto>>> deleteByNameLike(RequestDto requestDto,
                                                                              @RequestParam String name) {
        List<AddressBookDto> deletedEntries = addressBookService.deleteByNameLike(requestDto,name);

        // 삭제된 항목이 없으면 204 No Content 상태 코드를 반환
        if (deletedEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDto<>("삭제할 데이터가 없습니다.", false, null));
        } else {
            // 삭제된 항목이 있으면 200 OK 상태 코드와 함께 응답을 생성하여 반환
            return ResponseEntity.ok()
                    .body(new ResponseDto<>("데이터 삭제가 성공적으로 완료되었습니다.", true, deletedEntries));
        }
    }

    /**
     * 2.이름을 라이크 검색하여 해당 주소록 리스트를 조회
     *
     * @param requestDto
     * @param name
     * @return
     */
    @GetMapping("/searchByNameLike")
    public ResponseEntity<ResponseDto<List<AddressBookDto>>> searchByNameLike(RequestDto requestDto,
                                                                              @RequestParam String name) {
        List<AddressBookDto> result = addressBookService.searchByNameLike(requestDto, name);
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDto<>("주어진 이름으로 조회되는 데이터가 없습니다.", false, null));
        } else {
            return ResponseEntity.ok()
                    .body(new ResponseDto<>("주어진 이름으로 데이터를 조회하였습니다.", true, result));
        }
    }

    /**
     * 3. 폰넘버를 검색해서 해당 주소록 리스트를 조회한다.(조회)
     *
     * @param telephone
     * @return
     */
    @GetMapping("/searchByPhoneNumber")
    public ResponseEntity<ResponseDto<AddressBookDto>> searchByPhoneNumber(@RequestParam String telephone) {
        Optional<AddressBookDto> result = addressBookService.searchByTelephone(telephone);

        // 전화번호에 해당하는 주소록이 없는 경우
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDto<>("주어진 전화번호로 조회되는 데이터가 없습니다.", false, null));
        } else {
            return ResponseEntity.ok()
                    .body(new ResponseDto<>("주어진 전화번호로 데이터를 조회하였습니다.", true, result.get()));
        }
    }

    /**
     * 4. 주소를 라이크 검색해서 해당 주소록 리스트를 조회한다.(조회)
     *
     * @param requestDto
     * @param address
     * @return
     */
    @GetMapping("/searchByAddressLike")
    public ResponseEntity<ResponseDto<List<AddressBookDto>>> searchByAddressLike(RequestDto requestDto, @RequestParam String address) {
        List<AddressBookDto> result = addressBookService.searchByAddressLike(requestDto, address);

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDto<>("주어진 주소로 조회되는 데이터가 없습니다.", false, null));
        } else {
            return ResponseEntity.ok()
                    .body(new ResponseDto<>("주어진 주소로 데이터를 조회하였습니다.", true, result));
        }
    }

    /**
     * 5. 폰넘버를 받아 해당 내용의 데이터를 주소록에서 모두 삭제한다.(삭제)
     *
     * @param telephone
     */
    @DeleteMapping("/deleteByTelephone")
    public ResponseEntity<ResponseDto<AddressBookDto>> deleteByTelephone(@RequestParam String telephone) {
        Optional<AddressBookDto> deletedEntry = addressBookService.deleteByTelephone(telephone);
        // 삭제된 항목이 없으면 응답을 생성하여 반환
        if (deletedEntry.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDto<>("삭제할 데이터가 없습니다.", false, null));
        } else {
            return ResponseEntity.ok()
                    .body(new ResponseDto<>("데이터 삭제가 성공적으로 완료되었습니다.", true, deletedEntry.get()));
        }
    }

    /**
     * 6. 폰넘버를 받아 전달받은 주소록 데이터를 주소록에 변경 반영한다.(업데이트)
     *
     * @param telephone       업데이트할 주소록의 전화번호
     * @param addressBookDto  새로운 주소록 데이터
     * @param result          데이터 유효성 검증 결과
     * @return                업데이트 작업 결과에 대한 응답 (기존값, 업데이트된 값)
     */
    @PutMapping("/updateByTelephone")
    public ResponseEntity<ResponseDto> updateByTelephone(@RequestParam String telephone,
                                                         @Valid @RequestBody AddressBookDto addressBookDto,
                                                         BindingResult result) {
        // 변수 유효성 검증 실패의 경우
        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto("변수 유효성 검증 실패", false, result.getAllErrors()));
        } else {
            // 서비스를 호출하여 업데이트 결과를 받아옴
            ResponseDto response = addressBookService.updateByTelephone(telephone, addressBookDto);
            // 데이터가 없는 경우
            if (!response.isSuccess()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT)
                        .body(new ResponseDto("변경할 데이터가 없습니다.", false, response.getMessage()));
            } else {
                return ResponseEntity.ok()
                        .body(new ResponseDto<>("변경 완료 되었습니다.", true, (Map<String, AddressBookDto>) response.getData()));
            }
        }
    }
    /**
     * 7. email로 검색해서 해당 주소록 리스트를 조회한다.(조회)
     * @param email
     * @return
     */
    @GetMapping("/searchByEmail")
    public ResponseEntity<ResponseDto<AddressBookDto>> searchByEmail(@RequestParam String email) {
        Optional<AddressBookDto> result = addressBookService.searchByEmail(email);
        // 이메일에 해당하는 주소록이 없는 경우
        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(new ResponseDto<>("주어진 email로 데이터를 조회할 수 없습니다.", false, null));
        } else {
            return ResponseEntity.ok()
                    .body(new ResponseDto<>("주어진 email로 데이터를 조회하였습니다.", true, result.get()));
        }
    }


}