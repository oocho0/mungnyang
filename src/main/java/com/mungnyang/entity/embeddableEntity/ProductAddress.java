package com.mungnyang.entity.embeddableEntity;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProductAddress {

    private Double Lon;
    private Double Lat;

    private String detail;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductAddress productAddress = (ProductAddress) o;
        return Objects.equals(Lon, productAddress.Lon) && Objects.equals(Lat, productAddress.Lat) && Objects.equals(detail, productAddress.detail);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Lon, Lat, detail);
    }
}
