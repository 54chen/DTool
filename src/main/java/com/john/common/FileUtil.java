package com.john.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static byte[] readFile(String filename) throws IOException {
        byte[] data = null;
        try {
            Path path = Paths.get(filename);
            data = Files.readAllBytes(path);
        } catch (NoSuchFileException e) {
            return null;
        }
        return data;
    }

    public static boolean saveFile(String filename, byte[] content) throws IOException {
        try{
            Files.write(Paths.get(filename), content, StandardOpenOption.CREATE_NEW);
        } catch(FileAlreadyExistsException e){
            System.out.println(filename+" double writting");
        }
        return true;
    }

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