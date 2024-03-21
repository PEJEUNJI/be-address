package com.test.address.service;

import com.test.address.dto.AddressBookDto;
import com.test.address.utils.AddressBookBackupUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.List;

@Component
public class AddressBookBackup implements ApplicationContextAware {
    private static final Log log = LogFactory.getLog(AddressBookBackup.class);

    private ApplicationContext applicationContext;
    @Value("${file.backup.path}")
    private String backupFolderPath;

    @Value("${source.file.path}")
    private String sourceFilePath;

    @PreDestroy
    public void backupOnExit() {
        // 백업 메소드 호출
        AddressBookBackupUtil.backupAddressBook(backupFolderPath,sourceFilePath );
        // ApplicationContext를 통해 주소록 데이터를 가져옴
        AddressBookService addressBookService = applicationContext.getBean(AddressBookService.class);
        List<AddressBookDto> addressBookList = addressBookService.findAll();

        // 주소록을 CSV 파일로 생성
        String filePath = sourceFilePath; // 파일 경로 설정
        AddressBookBackupUtil.saveAddressBookToCSV(addressBookList, filePath);

        // 주소록을 백업한 내용을 확인하기 위한 로그 출력
        log.debug("주소록이 CSV 파일로 백업되었습니다. 파일 경로: " + filePath);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}