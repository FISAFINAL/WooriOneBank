package com.fisa.woorionebank.concert.domain.dto;

import com.fisa.woorionebank.concert.domain.entity.Area;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResponseDrawDTO {
    private String concertName;

    private String memberName;

    private Area area;

//    public static ResponseDrawDTO fromEntity()
}
