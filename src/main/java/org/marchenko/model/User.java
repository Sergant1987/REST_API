package org.marchenko.model;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
public class User {

    @Setter
    private Long id;

    @Setter
    private String name;

    @Setter
    private String phone;

    private final Map<Long, Record> records = new HashMap<>();

    public User(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public User(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return name.equals(user.name) && phone.equals(user.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

}
