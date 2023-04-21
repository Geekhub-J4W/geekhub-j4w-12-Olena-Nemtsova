package edu.geekhub.coursework.users;

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
}
