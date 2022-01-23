package com.sigmoid.dao;

import com.sigmoid.bean.Person;
import org.apache.hadoop.hbase.client.Table;

import java.io.IOException;

public interface HbaseDAO {
    void insertData(Table table, String row, Person person) throws IOException;
    int getCount(Table table) throws IOException;
}
