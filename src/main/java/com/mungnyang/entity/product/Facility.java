package com.mungnyang.entity.product;

import com.mungnyang.constant.IsTrue;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Objects;


@Embeddable
@Getter
@Setter
public class Facility {

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IsTrue isExist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return Objects.equals(name, facility.name) && Objects.equals(isExist, facility.isExist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isExist);
    }
}
