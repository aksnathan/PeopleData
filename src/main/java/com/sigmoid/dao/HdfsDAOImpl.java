package com.sigmoid.dao;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class HdfsDAOImpl implements HdfsDAO {

    private FileSystem fileSystem;
    private Configuration configuration;

    public HdfsDAOImpl(Configuration configuration) throws IOException {
        this.configuration = configuration;
        this.fileSystem = FileSystem.get(configuration);
    }

    @Override
    public void copyFile(String localSourcePath, String destinationHdfsPath) throws IOException {
        FileUtil.copy(new File(localSourcePath),
                fileSystem, new Path(destinationHdfsPath),
                false, configuration);
    }

    @Override
    public List<String> readFile(String filePath) throws IOException {
        FSDataInputStream inputStream = fileSystem.open(new Path(filePath));
        return IOUtils.readLines(inputStream, "UTF-8");
    }

    @Override
    public boolean isFileExists(String filePath) throws IOException {
        return fileSystem.exists(new Path(filePath));
    }

    @Override
    public void createDirectory(String directoryPath) throws IOException {
        Path dirPath = new Path(directoryPath);
        System.out.println("Checking whether HDFS dir exists:"+directoryPath);
        if(!fileSystem.exists(dirPath)){
            System.out.println("Creating HDFS dir:"+directoryPath);
            fileSystem.mkdirs(dirPath);
        }
        else{
            System.out.println("HDFS dir already exists:"+directoryPath);
        }
    }


}
