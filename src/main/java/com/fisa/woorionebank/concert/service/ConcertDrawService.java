package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.concert.domain.dto.response.WinnersCountDTO;
import com.fisa.woorionebank.concert.domain.entity.Area;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.saving.domain.entity.Saving;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertDrawService {
    private final ConcertHistoryRepository concertHistoryRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public WinnersCountDTO drawConcert(Long concertId) {
        List<Long> memberList = concertHistoryRepository.findMemberByStatusAndConcertId(Status.APPLY, concertId);

//        int seatAvailableR = 10_000;
//        int seatAvailableA = 30_000;
//        int seatAvailableB = 20_000;
        int seatAvailableR = 1;
        int seatAvailableA = 2;
        int seatAvailableB = 3;

        // R석 당첨자 뽑기 - 우리카드 실적 높은 사람(1만)
        Map<Integer, Member> winnersR = drawRandomWinners(seatAvailableR, createWinnerPool(memberList));
        updateWinners(winnersR, Area.R, concertId);

        // A석 당첨자 뽑기 - 적금 가입 고객(3만)
        List<ConcertHistory> concertHistoriesA = concertHistoryRepository.findByStatusAndConcertId(Status.APPLY, concertId);
        Map<Integer, Member> winnersA = Collections.emptyMap();

        if(!concertHistoriesA.isEmpty()) {
            winnersA = drawWinnersA(concertHistoriesA, seatAvailableA);
            updateWinners(winnersA, Area.A, concertId);
        }

        // B석 당첨자 뽑기 - 신청한 사람 중 랜덤(2만)
        List<ConcertHistory> concertHistoriesB = concertHistoryRepository.findByStatusAndConcertId(Status.APPLY, concertId);
        Map<Integer, Member> winnersB = Collections.emptyMap();

        if(!concertHistoriesB.isEmpty()) {
            winnersB = drawRandomWinners(seatAvailableB, createWinnerPool(concertHistoriesB.stream().map(concertHistory -> concertHistory.getMember().getMemberId()).collect(Collectors.toList())));
            updateWinners(winnersB, Area.B, concertId);
        }

        return WinnersCountDTO.builder()
                .winnersR(winnersR.size())
                .winnersA(winnersA.size())
                .winnersB(winnersB.size())
                .build();
    }

    private List<Member> createWinnerPool(List<Long> memberList) {
        List<Member> winnerPool = new ArrayList<>();

        for (Long id : memberList) {
            Member m = memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));
            int point = transferPoint(m.getGrade());

            for (int i = 0; i < point; i++) {
                winnerPool.add(m);
            }
        }
        return winnerPool;
    }

    // 회원 등급을 가산점으로 변환하는 함수
    private int transferPoint(Grade grade) {
        // 회원 등급을 가산점으로 변환하는 함수
        Map<Grade, Integer> map = new HashMap<>();
        Grade[] grades = Grade.values();
        int len = Grade.values().length;

        for (int i = 0; i < len; i++) {
            map.put(grades[i], len - i);
        }

        return map.get(grade);
    }

    // 랜덤으로 당첨자를 선발하여 Map으로 반환
    private Map<Integer, Member> drawRandomWinners(int numWinners, List<Member> winnerPool) {
        Map<Integer, Member> winners = new HashMap<>();
        Random random = new Random();
        for (int i = 0; i < numWinners && !winnerPool.isEmpty(); i++) {
            int index = random.nextInt(winnerPool.size());
            winners.put(i, winnerPool.remove(index));
        }
        return winners;
    }

    private Map<Integer, Member> drawWinnersA(List<ConcertHistory> concertHistoriesA, int seatAvailableA) {
        Map<Integer, Member> winners = new HashMap<>();
        List<Member> winnerPool = new ArrayList<>();
        Random random = new Random();

        for (ConcertHistory concertHistory : concertHistoriesA) {
            Member m = memberRepository.findById(concertHistory.getMember().getMemberId()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));
            List<Saving> saving = m.getSavings();

            if (!saving.isEmpty()) {
                winnerPool.add(concertHistory.getMember());
            }
        }

        int numWinnersA = Math.min(seatAvailableA, concertHistoriesA.size());

        for (int i = 0; i < numWinnersA; i++) {
            if (winnerPool.size() == 0) break;
            int index = random.nextInt(winnerPool.size());
            winners.put(i, winnerPool.remove(index));
        }
        return winners;
    }

    // 당첨자 업데이트
    private void updateWinners(Map<Integer, Member> winners, Area status, Long concertId) {
        for (Member winner : winners.values()) {
            ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(winner.getMemberId(), concertId).orElse(null);

            if (concertHistory != null) {
                concertHistory.win(status);
                concertHistoryRepository.save(concertHistory);
            }
        }
    }
}
