package com.mungnyang.entity.member;

import com.mungnyang.constant.MemberType;
import com.mungnyang.constant.Role;
import com.mungnyang.entity.Address;
import com.mungnyang.entity.DateEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "member")
@NoArgsConstructor
public class Member extends DateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false, length = 30)
    private String email;

    @Column
    private String password;

    @Embedded
    @Column
    private Address address;

    @Column(length = 15)
    private String tel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberType memberType;
}
