package com.mungnyang.entity;

import com.mungnyang.constant.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "member")

public class Member extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 30)
    private String email;
    @Column(nullable = false, length = 60)
    private String password;
    @Lob
    @Column(nullable = false)
    private String address;
    @Column(nullable = false, length = 15)
    private String tel;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

}
