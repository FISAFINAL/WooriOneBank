package com.fisa.woorionebank.member.entity;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.member.domain.dto.requestDto.registerDTO;
import com.fisa.woorionebank.saving.entity.Saving;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Table(name = "member")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long memberId;

    private String loginId;

    private String password;

    private String name;

    private int age;

    private String email;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @OneToMany(mappedBy = "member")
    private List<Saving> savings = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Account> accounts = new ArrayList<>();

    //Member 생성 메서드
    public static Member createMember(registerDTO registerDTO, String pw){
        Member member = new Member();
        member.loginId = registerDTO.getId();
        member.password = pw;
        member.name = registerDTO.getName();
        member.age = registerDTO.getAge();
        member.email = registerDTO.getEmail();
        member.grade = Grade.VIP;
        return member;
    }
}
