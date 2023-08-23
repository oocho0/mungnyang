package com.mungnyang.entity;

import com.mungnyang.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
public class Member extends DateEntity {

    public final static Member ANONYMOUS = new Member(null, "anonymous", "anonymous", null, null, null, Role.USER, "N");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;
    @Column(nullable = false, length = 10)
    private String name;
    @Column(nullable = false, length = 30)
    private String email;
    @Column(length = 60)
    private String password;
    @Embedded
    @Column
    private Address address;
    @Column(length = 15)
    private String tel;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;
    @Column(nullable = false)
    private String memberType;
}
