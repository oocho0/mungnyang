package com.mungnyang.entity.base;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "big_category")
public class BigCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bigCategoryId;

    private String bigCategoryName;
}
