package com.mungnyang.entity.product;

import lombok.*;

import javax.persistence.Embeddable;
import java.util.Objects;

@Getter
@Embeddable
@Setter
public class Image {

    private String name;
    private String fileName;
    private String url;
    private String isRepresentative;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Image imageInfo = (Image) o;
        return Objects.equals(name, imageInfo.name) && Objects.equals(fileName, imageInfo.fileName) && Objects.equals(url, imageInfo.url) && Objects.equals(isRepresentative, imageInfo.isRepresentative);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, fileName, url, isRepresentative);
    }
}
