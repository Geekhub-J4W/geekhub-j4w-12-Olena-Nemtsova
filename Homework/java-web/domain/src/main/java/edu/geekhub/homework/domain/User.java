package edu.geekhub.homework.domain;

import java.util.Objects;

public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private boolean isAdmin;

    public User() {
        this(null, null, null, null, null, false);
    }

    public User(String id, String firstName, String lastName,
                String password, String email, boolean isAdmin) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return "User{"
               + "id='" + id
               + "', firstName='" + firstName
               + "', lastName='" + lastName
               + "', email='" + email
               + "', isAdmin=" + isAdmin
               + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return isAdmin == user.isAdmin
               && Objects.equals(id, user.id)
               && Objects.equals(firstName, user.firstName)
               && Objects.equals(lastName, user.lastName)
               && Objects.equals(password, user.password)
               && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, password, email, isAdmin);
    }
}
