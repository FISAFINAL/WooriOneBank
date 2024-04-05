package com.fisa.woorionebank.member.domain.dto.requestDto;

import com.fisa.woorionebank.member.entity.Grade;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class RegisterDTO {

    private String id;

    private String password;

    private String name;

    private int age;

    private String email;

    private Grade grade = Grade.VVIP;
}
