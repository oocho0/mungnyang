package com.mungnyang.entity.member;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class MemberAddress {
    private String zipcode;
    private String main;
    private String detail;
    private String extra;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberAddress memberAddress = (MemberAddress) o;
        return Objects.equals(zipcode, memberAddress.zipcode) && Objects.equals(main, memberAddress.main) && Objects.equals(detail, memberAddress.detail) && Objects.equals(extra, memberAddress.extra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(zipcode, main, detail, extra);
    }
}
