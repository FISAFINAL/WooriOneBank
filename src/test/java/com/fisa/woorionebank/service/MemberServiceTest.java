package com.fisa.woorionebank.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.domain.dto.request.RegisterDTO;
import com.fisa.woorionebank.member.domain.dto.response.MemberDTO;
import com.fisa.woorionebank.member.entity.Grade;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import com.fisa.woorionebank.member.service.MemberService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("local")
@ExtendWith(SpringExtension.class) // 스프링이랑 JUnit 5를 통합해서 스프링 기능을 테스트에 사용가능 하게 하는 어노테이션!
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberService memberService;

//    @Test
//    public void saveMember() throws Exception{
//        //given
//        RegisterDTO registerDTO1 = new RegisterDTO("ID1", "PW1", "memeber1", 20, "email1", Grade.VVIP);
//
//        //when
//        MemberDTO member = memberService.createMember(registerDTO1);
//        Member member1 = (Member) member;
//
//        Member findMember = memberRepository.findById(member1.getMemberId())
//                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_Member));
//
//        //then
//        assertThat(member1.getMemberId()).isEqualTo(findMember.getMemberId());
//
//    }



}
