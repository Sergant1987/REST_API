package org.marchenko.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
public class Record {

    @Setter
    private Long id;

    @Setter
    private String name;

    @Setter
    private String phone;

    public Record(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public Record(Long id, String name, String phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record that = (Record) o;
        return name.equals(that.name) && phone.equals(that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phone);
    }

    @Override
    public String toString() {
        return "PhoneRecord{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
