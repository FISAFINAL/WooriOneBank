package com.fisa.woorionebank.concert.service;

import com.fisa.woorionebank.concert.domain.entity.Area;
import com.fisa.woorionebank.concert.domain.entity.ConcertHistory;
import com.fisa.woorionebank.concert.domain.entity.Status;
import com.fisa.woorionebank.concert.repository.jpa.ConcertHistoryRepository;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.saving.repository.saving.SavingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ConcertDrawService {
    private final ConcertHistoryRepository concertHistoryRepository;
    private final SavingRepository savingRepository;

    // 회원 등급을 가산점으로 변환하는 함수
    public int transferPoint(Grade grade) {
        // 회원 등급을 가산점으로 변환하는 함수
        Map<Grade, Integer> map = new HashMap<>();
        Grade[] grades = Grade.values();
        int len = Grade.values().length;

        for (int i = 0; i < len; i++) {
            map.put(grades[i], len - i);
        }

        return map.get(grade);
    }

    // 당첨자 업데이트
    public void updateWinner(Map<Integer, Member> winners, Area status, Long concertId) {
        for (Member winner : winners.values()) {
            ConcertHistory concertHistory = concertHistoryRepository.findByMemberIdAndConcertId(winner.getMemberId(), concertId).orElse(null);

            if (concertHistory != null) {
                concertHistory.win(status);
                concertHistoryRepository.save(concertHistory);
            }
        }
    }

    // 랜덤으로 당첨자 선발
    public void drawRandom(int numWinners, Map<Integer, Member> winners, List<Member> winnerPool) {

        Random random = new Random();

        // 랜덤으로 당첨자 선발
        for (int i = 0; i < numWinners && !winnerPool.isEmpty(); i++) {
            int index = random.nextInt(winnerPool.size());
            winners.put(0, winnerPool.remove(index));
        }
    }

    /* 좌석 당첨 로직 */
    // R석 우리카드 실적 높은 사람(1만)
    // A석 적금 가입 고객(3만)
    // B석 신청한 사람 중 랜덤(2만)
    @Transactional
    public void drawConcert(Long concertId) {
        List<Member> memberList = concertHistoryRepository.findMemberByConcertId(concertId);

        /*
         * seatAvailable 변수는 가용 좌석 수입니다.
         *  1. Concert entity에 컬럼 추가
         *  2. Seat(공연장 정보) entity 추가하는 방법 대신,
         * 우선 당첨 내역 확인 로직에서만 관리하고 있습니다.
         * */
        int seatAvailableR = 10_000;
        int seatAvailableA = 30_000;
        int seatAvailableB = 20_000;

        /* R석 당첨자 뽑기 */
        // 회원 등급을 가산점으로 변환하기
        Map<Integer, Member> winners = new HashMap<>();
        List<Member> winnerPool = new ArrayList<>();

        for (Member m : memberList) {
            int point = transferPoint(m.getGrade());

            // 우승자 풀에 추가 (가산점만큼 여러 번 추가)
            for (int i = 0; i < point; i++) {
                winnerPool.add(m);
            }
        }


        drawRandom(Math.min(seatAvailableR, memberList.size()), winners, winnerPool);
        updateWinner(winners, Area.R, concertId);
        // end of R석 당첨 로직

        /* A석 당첨자 뽑기 */
        List<ConcertHistory> concertHistories = concertHistoryRepository.findByStatusAndConcertId(Status.APPLY, concertId);

        // 만약 apply인 사람들이 없으면 이미 전원 R석 당첨입니다.
        if(!concertHistories.isEmpty()) {
            winners = new HashMap<>();
            winnerPool = new ArrayList<>();
            Random random = new Random();

            int numWinnersA = Math.min(seatAvailableA, concertHistories.size());

            // 랜덤으로 A석 당첨자 선발
            for (int i = 0; i < numWinnersA && !winnerPool.isEmpty(); i++) {
                // 적금 가입한 사용자만 선발한다.
                Optional<Long> saving = savingRepository.findByMemberId(concertHistories.get(i).getMember().getMemberId());
                if(!saving.isEmpty()) {
                    int index = random.nextInt(winnerPool.size());
                    winners.put(0, winnerPool.remove(index));
                }
            }

            updateWinner(winners, Area.A, concertId);

        }
        // end of A석 당첨

        /* B석 당첨자 뽑기 */
        List<ConcertHistory> concertHistoriesB = concertHistoryRepository.findByStatusAndConcertId(Status.APPLY, concertId);

        // 만약 apply인 사람들이 없으면 이미 전원 R석, A석 당첨입니다.
        if(!concertHistoriesB.isEmpty()) {
            winners = new HashMap<>();
            winnerPool = new ArrayList<>();

            drawRandom(Math.min(seatAvailableB, concertHistoriesB.size()), winners, winnerPool);
            updateWinner(winners, Area.B, concertId);

        }
        // end of B석 당첨
    }
}
