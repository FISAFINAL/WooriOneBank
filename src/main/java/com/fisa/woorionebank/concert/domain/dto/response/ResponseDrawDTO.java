package com.fisa.woorionebank.concert.domain.dto.response;

import com.fisa.woorionebank.concert.domain.entity.Area;
import lombok.*;

@AllArgsConstructor
@Getter
@Builder
@ToString
public class ResponseDrawDTO {
    private String concertName;
    private String memberName;
    private Area area;
}
