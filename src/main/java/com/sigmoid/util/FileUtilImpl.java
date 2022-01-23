package com.sigmoid.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileUtilImpl implements FileUtil{
    public static final String FILE_PATH_SEPARATOR = "/";
    @Override
    public File createFile(String filePath, String filename) throws IOException {
        File directory = new File(filePath);
        if(!directory.exists()) directory.mkdir();
        if(!directory.isDirectory()) throw new IOException("Not a directory:"+filePath);
        File file = new File(filePath + FILE_PATH_SEPARATOR + filename);
        if(!file.exists()) file.createNewFile();
        return file;
    }

    @Override
    public <T> void writeFile(File file, List<T> list) throws IOException{
        FileWriter fileWriter = new FileWriter(file);
        for(T obj: list){
            fileWriter.write(obj.toString());
            fileWriter.write("\n");
        }
        fileWriter.close();
    }
}
