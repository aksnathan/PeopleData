package com.sigmoid.dao;

import java.io.IOException;
import java.util.List;

public interface HdfsDAO {
    void copyFile(String loaclSourcePath, String destinationHdfsPath) throws IOException;
    List<String> readFile(String filePath) throws IOException;
    boolean isFileExists(String filePath) throws IOException;
    void createDirectory(String directoryPath) throws IOException;
}
