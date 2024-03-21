package com.test.address.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class CsvFileReader {
    public static List<String> read(String filename, boolean skipHeader) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = CsvFileReader.class.getClassLoader().getResourceAsStream(filename);
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String line;
            boolean isHeader = true; // 첫 번째 라인인지 여부를 나타내는 플래그
            while ((line = bufferedReader.readLine()) != null) {
                if (!isHeader || !skipHeader) {
                    lines.add(line);
                }
                isHeader = false;
            }
        }
        return lines;
    }
}
