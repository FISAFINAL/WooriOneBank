package com.fisa.woorionebank.member.entity;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.common.BaseEntity;
import com.fisa.woorionebank.member.domain.dto.request.RegisterDTO;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public static Member createMember(RegisterDTO registerDTO, String pw){
        Member member = new Member();
        member.loginId = registerDTO.getId();
        member.password = pw;
        member.name = registerDTO.getName();
        member.age = registerDTO.getAge();
        member.email = registerDTO.getEmail();
        member.grade = Grade.VIP;
        return member;
    }

    //연관관계 메소드
    public void addAccount(Account account){
        accounts.add(account);
    }

    public void addSaving(Saving saving) {
        savings.add(saving);
    }

    public Member(String loginId, String password, String name, int age, String email, Grade grade) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
        this.age = age;
        this.email = email;
        this.grade = grade;
    }
}
