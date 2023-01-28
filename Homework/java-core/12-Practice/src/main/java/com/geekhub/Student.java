package com.geekhub;

public class Student {
    private static int COUNTER = 0;

    private final int id;
    private final String firstName;
    private final String lastName;

    public Student(String firstName, String lastName) {
        this(++COUNTER, firstName, lastName);
    }

    public Student(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static void setMaxId(int id) {
        COUNTER = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Student{ " +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            " }";
    }
}
