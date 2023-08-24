package com.mungnyang.entity.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberAddress {
    private String zipcode;
    private String address;
    private String detail;
    private String addition;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAddress address1 = (MemberAddress) o;
        return Objects.equals(zipcode, address1.zipcode) && Objects.equals(address, address1.address) && Objects.equals(detail, address1.detail) && Objects.equals(addition, address1.addition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipcode, address, detail, addition);
    }
}
