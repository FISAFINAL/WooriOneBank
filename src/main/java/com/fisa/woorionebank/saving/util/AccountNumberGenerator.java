package com.fisa.woorionebank.saving.util;

import java.util.Set;

public class AccountNumberGenerator {

    private static final int MIN_ACCOUNT_NUMBER = 100_000_000;
    private static final int MAX_ACCOUNT_NUMBER = 999_999_999;

    /**
     * 새로운 적금 계좌 번호 생성
     * @param existingAccountNumbers 이미 존재하는 계좌 번호 목록
     * @return 생성된 적금 계좌 번호
     */
    public static String generateUniqueAccountNumber(Set<String> existingAccountNumbers) {
        while (true) {
            // 랜덤한 숫자 생성
            int randomNumber = generateRandomAccountNumber();
            String accountNumber = String.valueOf(randomNumber);

            // 생성한 숫자가 이미 존재하는지 확인
            if (!existingAccountNumbers.contains(accountNumber)) {
                // 1002 + 9자리 (우리 계좌 형식)
                return "1002" + accountNumber;
            }
        }
    }

    /**
     * MIN_ACCOUNT_NUMBER와 MAX_ACCOUNT_NUMBER 사이의 랜덤한 숫자 생성
     * @return 생성된 랜덤 숫자
     */
    private static int generateRandomAccountNumber() {
        return (int) (Math.random() * (MAX_ACCOUNT_NUMBER - MIN_ACCOUNT_NUMBER + 1)) + MIN_ACCOUNT_NUMBER;
    }

}
