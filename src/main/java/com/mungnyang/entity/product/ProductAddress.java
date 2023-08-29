package com.mungnyang.entity.product;

import com.mungnyang.entity.member.Address;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class ProductAddress {

    private Double Lon;
    private Double Lat;
    @Embedded
    @Column
    private Address address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductAddress productAddress = (ProductAddress) o;
        return Objects.equals(Lon, productAddress.Lon) && Objects.equals(Lat, productAddress.Lat) && Objects.equals(address, productAddress.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Lon, Lat, address);
    }
}
