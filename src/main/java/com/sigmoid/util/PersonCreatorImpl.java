package com.sigmoid.util;

import com.sigmoid.bean.Person;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PersonCreatorImpl  implements  PersonCreator{

    public static final int MIN_AGE_LIMIT = 24;
    public static final int MAX_AGE_LIMIT = 58;
    public static final long MIN_PHONE_LIMIT = 9000011111L;
    public static final long MAX_PHONE_LIMIT = 9999999999L;
    public static final String NAME_TAG = "NAME";
    public static final String ADDRESS_TAG = "ADDRESS";
    public static final String SEPARATOR = "_";
    public static final String COMPANY_TAG = "COMPANY";
    public static final String BUILDING_CODE_TAG = "BUILDING_CODE";
    public static final int RANDOM_LIMIT = 10;
    public static final int RANDOM_PHONE_LIMIT = 9999999;
    private final Random random = new Random();

    private int getRandomAge(){
        return MIN_AGE_LIMIT + random.nextInt(MAX_AGE_LIMIT - MIN_AGE_LIMIT);
    }

    private int getRandomNumber(){
        return  random.nextInt(RANDOM_LIMIT);
    }

    private long getRandomPhoneNumber(){
        return  MIN_PHONE_LIMIT + random.nextInt(RANDOM_PHONE_LIMIT);
    }

    @Override
    public List<Person> createPersons(int count, String fileTag) throws IOException {
        if(count <=0 || count >100)
            throw new IOException("Invalid value for Count(Accepted Range 1 to 100):"+count);
        if(fileTag == null) throw new IOException("Got Null Value for fileTag");
        List<Person> personList = new ArrayList<>();
        for(int i = 1; i<=count; i++){
            Person.Builder personBuilder = Person.Builder.newInstance();
            personBuilder
                    .setName(fileTag + SEPARATOR + NAME_TAG + SEPARATOR+i)
                    .setAddress(ADDRESS_TAG + SEPARATOR + i)
                    .setAge(getRandomAge())
                    .setCompany(COMPANY_TAG + SEPARATOR + getRandomNumber())
                    .setBuildingCode(BUILDING_CODE_TAG + SEPARATOR + getRandomNumber())
                    .setPhoneNumber(String.valueOf(getRandomPhoneNumber()));
            Person personObj = personBuilder.build();
            personList.add(personObj);
        }
        return personList;
    }
}
