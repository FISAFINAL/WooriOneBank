package com.fisa.woorionebank.saving.domain.responsedto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
public class SavingHistoryListDTO {
    private List<SavingHistoryDTO> savingHistoryList = new ArrayList<>();
}
