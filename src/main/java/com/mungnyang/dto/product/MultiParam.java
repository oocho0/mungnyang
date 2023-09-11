package com.mungnyang.dto.product;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MultiParam {
    private List<Long> categoryId;
    private List<Long> cityId;
}
