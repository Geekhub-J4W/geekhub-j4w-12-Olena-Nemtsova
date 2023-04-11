package edu.geekhub.homework.users;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER("USER"),
    SELLER("SELLER"),
    ADMIN("ADMIN"),
    SUPER_ADMIN("SUPER_ADMIN");
    private final String name;

    Role(String name) {
        this.name = name;
    }

    @JsonValue
    public String getName() {
        return name;
    }

    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Role forValues(@JsonProperty("name") String name) {
        for (Role role : Role.values()) {
            if (
                role.name.equals(name)) {
                return role;
            }
        }
        return null;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
