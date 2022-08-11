package com.john.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static List<String> readFileByLines(String fileName) throws IOException {
        List<String> result = new ArrayList<>();
        FileReader fr = new FileReader(new File(fileName));
        BufferedReader reader = new BufferedReader(fr);
        String tempString = null;
        while ((tempString = reader.readLine()) != null) {
            result.add(tempString);
        }
        reader.close();
        fr.close();
        return result;
    }

    public static void saveFileByLine(String fileName, String line) throws IOException {
        FileWriter fw = new FileWriter(new File(fileName));
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(line + "\r\n");
        bw.close();
        fw.close();
    }
}