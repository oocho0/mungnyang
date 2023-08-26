package com.mungnyang.entity.fixedEntity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "big_category")
public class BigCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bigCategoryId;

    private String name;
}
