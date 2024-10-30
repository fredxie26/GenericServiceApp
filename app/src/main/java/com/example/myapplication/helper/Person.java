package com.example.myapplication.helper;

public class Person {
    private String firstName;
    private String lastName;
    private String photoPath;
    private String address;
    private String status;

    public Person(String firstName, String lastName, String photoPath, String address, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoPath = photoPath;
        this.address = address;
        this.status = status;
    }

    public String getFullInfo() {
        return "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + address + "\n" +
                "Status: " + status;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
