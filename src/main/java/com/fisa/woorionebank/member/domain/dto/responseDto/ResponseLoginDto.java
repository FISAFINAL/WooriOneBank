package com.fisa.woorionebank.member.domain.dto.responseDto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseLoginDto {

    private String id;
    private String password;
    private String token;
}
