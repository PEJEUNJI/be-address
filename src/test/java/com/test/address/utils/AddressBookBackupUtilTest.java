package com.test.address.utils;
import com.test.address.dto.AddressBookDto;
import com.test.address.service.AddressBookLoader;
import com.test.address.service.AddressBookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;


@SpringBootTest
class AddressBookBackupUtilTest {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private AddressBookLoader addressBookLoader;

    @Test
    void testBackupAddressBook() {
        String sourceFilePath = "src/main/resources/backup_test_success_address.csv";
        String backupFolderPath = "src/main/resources/backup/test/";

        // 백업 메소드 호출
        AddressBookBackupUtil.backupAddressBook(backupFolderPath,sourceFilePath);
    }

    @Test
    void testSaveAddressBookToCSV() throws IOException {

        List<AddressBookDto> addressBookList = addressBookService.findAll();
        // when
        AddressBookBackupUtil.saveAddressBookToCSV(addressBookList, "src/main/resources/backup_test_success_address.csv");
    }
}