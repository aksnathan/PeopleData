package com.sigmoid.main;


//import java.lang.module.Configuration;

import com.sigmoid.bean.Person;
import com.sigmoid.dao.HbaseDAO;
import com.sigmoid.dao.HbaseDAOImpl;
import com.sigmoid.dao.HdfsDAO;
import com.sigmoid.dao.HdfsDAOImpl;
import com.sigmoid.util.FileUtil;
import com.sigmoid.util.FileUtilImpl;
import com.sigmoid.util.PersonCreator;
import com.sigmoid.util.PersonCreatorImpl;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PersonDataMain {

    private static final String HDFS_FILE_SYSTEM_PROPERTY_TAG = "fs.defaultFS";
    private static final String HDFS_FILE_SYSTEM_URL = "hdfs://localhost:9000";
    private static final String HDFS_DIRECTORY_PATH = "/user/senthilnathan/person";
    private static final String FILENAME_TAG = "PERSON_FILE_";
    private static final String FILE_EXTENSION = ".csv";
    private static final String LOCAL_DIRECTORY_PATH = "personFileDir";
    private static final String SEPARATOR = "/";
    private static final int FILE_COUNT = 100;
    private static final int PERSON_COUNT_PER_FILE = 100;
    private static final String[] COLUMN_FAMILIES = {"Personal", "Professional"};
    private static final String TABLE_NAME = "Employee";

    private static Configuration configuration;
    private static PersonCreator personCreator;
    private static FileUtil fileUtil;
    private static HdfsDAO hdfsDAO;
    private static Configuration hBaseConfiguration;
    private static HbaseDAO hBaseDAO;
    private static Table employeeTable;
    private static Admin hBaseAdmin;
    private static TableName employeeTableName;


    private static void setUpConfiguration() throws IOException {
        configuration = new Configuration();
        configuration.set(HDFS_FILE_SYSTEM_PROPERTY_TAG, HDFS_FILE_SYSTEM_URL);
        personCreator = new PersonCreatorImpl();
        fileUtil = new FileUtilImpl();
        hdfsDAO = new HdfsDAOImpl(configuration);
        hBaseConfiguration = HBaseConfiguration.create();
        HBaseAdmin.available(hBaseConfiguration);
        hBaseDAO = new HbaseDAOImpl();
        employeeTableName = TableName.valueOf(TABLE_NAME);
        Connection connection = ConnectionFactory.createConnection(hBaseConfiguration);
        hBaseAdmin = connection.getAdmin();
        employeeTable = connection.getTable(employeeTableName);
    }

    private static void createLocalFiles() throws IOException {
        for(int i=1; i<=FILE_COUNT; i++) {
            System.out.println("Creating Person Object(s) for File:" + FILENAME_TAG + i);
            List<Person> personList = personCreator.createPersons(PERSON_COUNT_PER_FILE, FILENAME_TAG + i);
            System.out.println("Creating File:" + FILENAME_TAG + i);
            String localFilePath = FILENAME_TAG + i + FILE_EXTENSION;
            File file = fileUtil.createFile(LOCAL_DIRECTORY_PATH, localFilePath);
            System.out.println("Writing to File:" + FILENAME_TAG + i);
            fileUtil.writeFile(file, personList);
        }
    }

    private static void copyFilesToHdfs() throws IOException {
        for(int i=1; i<=FILE_COUNT; i++){
            System.out.println("Check whether file:"+ FILENAME_TAG + i +" already exists in HDFS");
            String hdfsFilePath = HDFS_DIRECTORY_PATH + SEPARATOR + FILENAME_TAG + i + FILE_EXTENSION;
            hdfsDAO.createDirectory(HDFS_DIRECTORY_PATH);
            if(hdfsDAO.isFileExists(hdfsFilePath)){
                System.out.println("File already exists (ignoring copy file):"+hdfsFilePath);
            }
            else{
                System.out.println("Copying to HDFS file:"+FILENAME_TAG + i);
                hdfsDAO.copyFile(LOCAL_DIRECTORY_PATH + SEPARATOR + FILENAME_TAG + i + FILE_EXTENSION, hdfsFilePath);
            }
        }
    }

    private static void createTableInHdfs() throws IOException {
        System.out.println("Checking whether Employee table exists in HBASE....");
        if(!hBaseAdmin.tableExists(employeeTableName)) {
            System.out.println("Creating Employee table in HBASE....");
            TableDescriptorBuilder tableDescBuilder = TableDescriptorBuilder.newBuilder(employeeTableName);
            List<ColumnFamilyDescriptor> columnDescList = new ArrayList<>();
            for (String columnFamily : COLUMN_FAMILIES) {
                ColumnFamilyDescriptor columnFamilyDescriptor =
                        ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes(columnFamily)).build();
                columnDescList.add(columnFamilyDescriptor);
            }
            tableDescBuilder.setColumnFamilies(columnDescList);
            hBaseAdmin.createTable(tableDescBuilder.build());
        }
        else{
            System.out.println("Employee table already exists");
        }
    }

    public static void main(String[] args) {
        try {

            setUpConfiguration();
            createLocalFiles();
            copyFilesToHdfs();
            createTableInHdfs();
            insertRowsInHdfsTable();
            printRecordCount();
      }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    private static void printRecordCount() throws IOException {
        System.out.println("Getting Employee table count...");
        int count = hBaseDAO.getCount(employeeTable);
        System.out.println("Number of records in 'Employee' table:"+count);
    }

    private static void insertRowsInHdfsTable() throws IOException {
        for(int i=1; i<=FILE_COUNT; i++){
            System.out.println("Reading file from Hdfs:"+ FILENAME_TAG + i);
            String hdfsFilePath = HDFS_DIRECTORY_PATH + SEPARATOR + FILENAME_TAG + i + FILE_EXTENSION;
            List<String> csvList = hdfsDAO.readFile(hdfsFilePath);
            System.out.println("Inserting person object(s) in HBASE for file:"+ FILENAME_TAG + i);
            for(String personCsvString: csvList){
                Person person = Person.valueOf(personCsvString);
                hBaseDAO.insertData(employeeTable, person.getName(), person);
            }
        }
    }



}
