package com.fisa.woorionebank.member.domain.dto.request;

import com.fisa.woorionebank.member.entity.Grade;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class RegisterDTO {
    private String id;

    private String password;

    private String name;

    private int age;

    private String email;

    private Grade grade = Grade.NONE;
}
