package com.mungnyang.dto.product;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchStoreFilter {
    private Long byCity;
    private Long byState;
    private Long byBigCategory;
    private Long bySmallCategory;
    private String byStoreStatus;
    private String byStoreName;
}
