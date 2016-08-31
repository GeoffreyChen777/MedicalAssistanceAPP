package com.sorry.model;

public class PersonalData {

    private int sex;
    private int age;
    private int height;
    private int weight;
    private String name;
    private String emergencyNumber;


    public PersonalData(){

    }

    public PersonalData(int sex, int age, int height, int weight, String name, String emergencyNumber) {
        this.sex = sex;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.name = name;
        this.emergencyNumber = emergencyNumber;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmergencyNumber() {
        return emergencyNumber;
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }
}
