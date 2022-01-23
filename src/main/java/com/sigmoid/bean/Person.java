package com.sigmoid.bean;

import java.util.StringJoiner;
import java.util.StringTokenizer;

final public class Person{
    private final String name;
    private final int age;
    private final String company;
    private final String buildingCode;

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getCompany() {
        return company;
    }

    public String getBuildingCode() {
        return buildingCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    private final String phoneNumber;
    private final String address;

    public Person(Builder builder){
        this.name = builder.name;
        this.age = builder.age;
        this.company = builder.company;
        this.buildingCode = builder.buildingCode;;
        this.phoneNumber = builder.phoneNumber;
        this.address = builder.address;
    }

    @Override
    public String toString(){
        StringJoiner joiner = new StringJoiner(",");
        joiner.add(this.name);
        joiner.add(Integer.toString(this.age));
        joiner.add(this.company);
        joiner.add(this.buildingCode);
        joiner.add(this.phoneNumber);
        joiner.add(this.address);

        return joiner.toString();
    }

    public static Person valueOf(String csvString){
        if(csvString == null) return null;
        StringTokenizer stringTokenizer = new StringTokenizer(csvString, ",");
        Builder builder = Builder.newInstance();
        builder
                .setName(stringTokenizer.nextToken())
                .setAge(Integer.parseInt(stringTokenizer.nextToken()))
                .setCompany(stringTokenizer.nextToken())
                .setBuildingCode(stringTokenizer.nextToken())
                .setPhoneNumber(stringTokenizer.nextToken())
                .setAddress(stringTokenizer.nextToken());
        return builder.build();
    }

    public static class Builder{
        private String name;
        private int age;
        private String company;
        private String buildingCode;
        private String phoneNumber;
        private String address;

        public static Builder newInstance(){
            return new Builder();
        }

        private Builder(){
        }

        public Builder setName(String name){
            this.name = name;
            return this;
        }

        public Builder setAge(int age){
            this.age = age;
            return this;
        }

        public Builder setCompany(String company){
            this.company = company;
            return this;
        }

        public Builder setBuildingCode(String buildingCode){
            this.buildingCode = buildingCode;
            return this;
        }

        public Builder setPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder setAddress(String address){
            this.address = address;
            return this;
        }

        public Person build(){
            return new Person(this);
        }
    }

}
