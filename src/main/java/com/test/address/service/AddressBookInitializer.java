package com.test.address.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AddressBookInitializer {

    private final AddressBookLoader addressBookLoader;
    private final String filePath;

    @Autowired
    public AddressBookInitializer(AddressBookLoader addressBookLoader, @Value("${file.name}") String filePath) {
        this.addressBookLoader = addressBookLoader;
        this.filePath = filePath;
    }

    @PostConstruct
    public void initializeAddressBook() {
        // 어플리케이션 실행 시 메모리에 데이터 로드
        addressBookLoader.loadFromFile(filePath);
    }
}

