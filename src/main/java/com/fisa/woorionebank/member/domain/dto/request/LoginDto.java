package com.fisa.woorionebank.member.domain.dto.request;

import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    private String id;
    private String password;
}
