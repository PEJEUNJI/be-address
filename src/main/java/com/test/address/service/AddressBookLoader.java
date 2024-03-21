package com.test.address.service;

import com.test.address.dto.AddressBookDto;
import com.test.address.utils.CsvFileReader;
import com.test.address.utils.ValidationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class AddressBookLoader {

    private static final Log log = LogFactory.getLog(AddressBookLoader.class);


    private final AddressBookService addressBookService;
    @Autowired
    public AddressBookLoader( AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    public void loadFromFile(String filePath) {
        try {
            List<String> lines = CsvFileReader.read(filePath, true);

            for (String line : lines) {
                AddressBookDto addressBookDto = parseAddressBookFromCsv(line);
                if (addressBookDto != null) {
                    String email = addressBookDto.getEmail();

                    if (addressBookService.isDuplicateEmail(email)) {
                        log.error("중복된 이메일 주소: " + email);
                        continue;
                    }
                    AddressBookDto entry = new AddressBookDto(addressBookDto.getTelephone(), email, addressBookDto.getAddress(), addressBookDto.getName());
                    addressBookService.saveAddressBookEntry(entry);
                }
            }
        } catch (IOException e) {
            log.error("파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage());
            //System.err.println("파일을 읽는 도중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    /**
     * 데이터 형식
     * 전화번호는 010 으로 시작해야 합니다.
     * 전화번호 형식은 0101231234 또는 010-123-1234 또는 010-1234-1234 이어야 합니다.
     * 이메일 형식은 아이디@도메인 이어야 합니다.
     */
    private AddressBookDto parseAddressBookFromCsv(String csvLine) {
        // Sample : 서울시 광진구,01000000000,hong@test.com,홍길동
        String[] data = csvLine.split(",");
        if (data.length != 4) {
            log.error("누락된 데이터 존재 - " + csvLine);
           // System.err.println("누락된 데이터 존재 - " + csvLine);
            return null;
        }

        String phoneNumber = data[1].trim();
        String email = data[2].trim();
        String address = data[0].trim();
        String name = data[3].trim();

        if (!ValidationUtil.isValidPhoneNumber(phoneNumber) || !ValidationUtil.isValidEmail(email)) {
            log.error("형식을 만족하지 못하는 데이터 :" + csvLine);
            //System.err.println("형식을 만족하지 못하는 데이터 :" + csvLine);
            return null;
        }
        return new AddressBookDto(phoneNumber, email, address, name);
    }


}