package com.example.myapplication.helper;

import java.util.HashSet;
import java.util.Set;

public class Person {
    private int id;
    private String firstName;
    private String lastName;
    private String photoPath;
    private String address;
    private Set<String> statuses;

    public Person(int id, String firstName, String lastName, String photoPath, String address, Set<String> statuses) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photoPath = photoPath;
        this.address = address;
        this.statuses = statuses;
    }

    // Accessor for photoPath
    public String getPhotoPath() {
        return photoPath;
    }

    public String getFullInfo() {
        return "Name: " + firstName + " " + lastName + "\n" +
                "Address: " + address + "\n" +
                "Status: " + String.join(", ", statuses);
    }

    public Set<String> getStatuses() {
        return statuses;
    }

    public int getId() {
        return id;
    }

    public void addStatus(String status) {
        statuses.add(status);
    }

    public void removeStatus(String status) {
        statuses.remove(status);
    }
}
