package com.mungnyang.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
//@ConstructorBinding
//@ConfigurationProperties("path")
public final class Path {
    private final String storeImagePath = "images/store/";
    private final String roomImagePath = "images/room/";
    private final String accomImagePath = "images/accommodation/";
    private final String uploadPath = "files:///images/";
}
