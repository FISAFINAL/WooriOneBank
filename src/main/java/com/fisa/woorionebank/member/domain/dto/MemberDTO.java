package com.fisa.woorionebank.member.domain.dto;

import com.fisa.woorionebank.account.entity.Account;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class MemberDTO {
    private Long memberId;

    private String id;

    private String password;

    private String name;

    private int age;

    private String email;

    private Grade grade;

    private List<Saving> saving;

    private List<Account> accounts;

    private String token;

    private Boolean isVerified; // 아이디 중복 체크

}
