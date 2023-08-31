package com.mungnyang.entity;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class Address {
    private String zipcode;
    private String main;
    private String detail;
    private String extra;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(zipcode, address.zipcode) && Objects.equals(main, address.main) && Objects.equals(detail, address.detail) && Objects.equals(extra, address.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipcode, main, detail, extra);
    }
}
