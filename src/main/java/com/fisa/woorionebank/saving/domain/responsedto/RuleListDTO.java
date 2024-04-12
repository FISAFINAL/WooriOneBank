package com.fisa.woorionebank.saving.domain.responsedto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class RuleListDTO {
    private List<SavingRuleDTO> savingRuleList = new ArrayList<>();

}
