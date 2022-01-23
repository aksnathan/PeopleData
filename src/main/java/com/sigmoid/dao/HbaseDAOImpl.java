package com.sigmoid.dao;

import com.sigmoid.bean.Person;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Iterator;

public class HbaseDAOImpl implements HbaseDAO{

    String[] columns = {"Name", "Age", "Company", "BuildingCode", "PhoneNumber", "Address"};
    String[] columnFamilies = {"Personal", "Professional"};

    @Override
    public void insertData(Table table, String row, Person person) throws IOException {
        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(columnFamilies[0].getBytes(), columns[0].getBytes(), Bytes.toBytes(person.getName()));
        put.addColumn(columnFamilies[0].getBytes(), columns[1].getBytes(), Bytes.toBytes(person.getAge()));
        put.addColumn(columnFamilies[0].getBytes(), columns[4].getBytes(), Bytes.toBytes(person.getPhoneNumber()));
        put.addColumn(columnFamilies[0].getBytes(), columns[5].getBytes(), Bytes.toBytes(person.getAddress()));
        put.addColumn(columnFamilies[1].getBytes(), columns[2].getBytes(), Bytes.toBytes(person.getCompany()));
        put.addColumn(columnFamilies[1].getBytes(), columns[3].getBytes(), Bytes.toBytes(person.getBuildingCode()));
        table.put(put);
    }

    @Override
    public int getCount(Table table) throws IOException {
        ResultScanner resultScanner = table.getScanner(new Scan());
        Iterator<Result> iterator = resultScanner.iterator();

        int count = 0;
        while(iterator.hasNext()){
            count++;
            iterator.next();
        }
        return count;
    }

}
