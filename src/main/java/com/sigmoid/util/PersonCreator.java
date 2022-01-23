package com.sigmoid.util;

import com.sigmoid.bean.Person;

import java.io.IOException;
import java.util.List;

public interface PersonCreator {
    public List<Person> createPersons(int count, String fileTag) throws IOException;
}
