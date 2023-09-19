package com.mungnyang.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties("path")
public final class Path {
    private final String storeImagePath;
    private final String roomImagePath;
    private final String accomImagePath;
    private final String uploadPath;
}
