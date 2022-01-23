package com.sigmoid.util;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface FileUtil{
    File createFile(String filePath, String filename) throws IOException;
    <T> void writeFile(File file, List<T> list) throws IOException;
}
