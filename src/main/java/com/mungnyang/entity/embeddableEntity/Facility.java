package com.mungnyang.entity.embeddableEntity;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;


@Embeddable
@Getter
@Setter
public class Facility {

    private String facilityName;
    private String isExist;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Facility facility = (Facility) o;
        return Objects.equals(facilityName, facility.facilityName) && Objects.equals(isExist, facility.isExist);
    }

    @Override
    public int hashCode() {
        return Objects.hash(facilityName, isExist);
    }
}
