package com.fisa.woorionebank.member.domain.dto.response;

import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private String name;
    private int age;
    private String email;
    private Grade grade;

    public static MemberDTO fromEntity(Member member) {
        return MemberDTO.builder()
                .name(member.getName())
                .age(member.getAge())
                .email(member.getEmail())
                .grade(member.getGrade())
                .build();
    }

}
