package edu.geekhub.coursework.users;

import java.util.Objects;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private Role role;

    public User(
        int id,
        String firstName,
        String lastName,
        String password,
        String email,
        Role role
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(User user) {
        this(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getPassword(),
            user.getEmail(),
            user.getRole()
        );
    }

    public User() {
        this(-1, null, null, null, null, null);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{"
               + "id=" + id
               + ", firstName='" + firstName + '\''
               + ", lastName='" + lastName + '\''
               + ", password='" + password + '\''
               + ", email='" + email + '\''
               + ", role=" + role
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

        return id == user.id
               && Objects.equals(firstName, user.firstName)
               && Objects.equals(lastName, user.lastName)
               && Objects.equals(password, user.password)
               && Objects.equals(email, user.email)
               && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, password, email, role);
    }
}
