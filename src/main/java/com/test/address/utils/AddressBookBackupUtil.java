package com.test.address.utils;

import com.test.address.dto.AddressBookDto;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class AddressBookBackupUtil {

    // 시리얼 번호를 저장할 AtomicLong 변수 생성
    private static final AtomicLong serialNumber = new AtomicLong(0);
    private static final Log log = LogFactory.getLog(AddressBookBackupUtil.class);


    @Value("${file.backup.path}")
    private String backupFolderPath;
    @Value("${file.path}")
    private String sourceFilePath;
    @Value("${file.name}")
    private String sourceFileName;

    public static void backupAddressBook(@Value("${file.backup.path}") String backupFolderPath, @Value("${source.file.path}") String sourceFilePath) {
        // 백업 파일 경로
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        // 파일 이름 생성에 시리얼 번호 추가
        String backupFileName = "backup_address_" + LocalDateTime.now().format(formatter) + "_" + serialNumber.getAndIncrement() + ".csv";

        String backupFilePath = backupFolderPath + backupFileName;

        // 기존 파일을 백업 파일로 복사
        try {
            Files.copy(Paths.get(sourceFilePath), Paths.get(backupFilePath));
            log.debug("기존 파일을 백업 파일로 복사했습니다. 파일명: " + backupFileName);
            //System.out.println("기존 파일을 백업 파일로 복사했습니다. 파일명: " + backupFileName);
        } catch (IOException e) {
            log.debug("기존 파일을 백업하는 중에 오류가 발생했습니다.");
            //System.err.println("기존 파일을 백업하는 중에 오류가 발생했습니다.");
            e.printStackTrace();
        }
    }

    public static void saveAddressBookToCSV(List<AddressBookDto> addressBookList, String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // CSV 헤더 작성
            writer.write("\"주소\",\"연락처\",\"이메일\",\"이름\"\n");

            // 주소록 데이터를 CSV 파일로 저장
            for (AddressBookDto addressBook : addressBookList) {
                String line = addressBook.getAddress() + "," +
                        addressBook.getTelephone() + "," +
                        addressBook.getEmail() + "," +
                        addressBook.getName();
                writer.write(line);
                writer.newLine();
            }
            log.debug("CSV 파일이 생성되었습니다.");
        } catch (IOException e) {
            log.error("CSV 파일 생성 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    public static void deleteFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            Files.deleteIfExists(path);
            log.debug("파일을 삭제했습니다. 경로: " + filePath);
        } catch (Exception e) {
            log.debug("파일 삭제 중 오류가 발생했습니다: " + e.getMessage());
        }
    }
}