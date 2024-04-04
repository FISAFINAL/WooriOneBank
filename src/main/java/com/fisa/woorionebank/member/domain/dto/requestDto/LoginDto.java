package com.fisa.woorionebank.member.domain.dto.requestDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoginDto {

    private String id;
    private String password;
}
