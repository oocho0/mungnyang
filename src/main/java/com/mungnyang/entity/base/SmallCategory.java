package com.mungnyang.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Table(name = "small_category")
public class SmallCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long smallCategoryId;

    private String smallCategoryName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "big_category")
    private Long bigCategoryId;
}
