package com.fisa.woorionebank.member.service;

import com.fisa.woorionebank.member.domain.dto.requestDto.registerDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public Member createMember(registerDTO registerDTO) {
        if (registerDTO == null) {
            throw new RuntimeException("Invalid arguments");
        }

        if (memberRepository.existsByLoginId(registerDTO.getEmail())) {
            log.warn("UserId already exists {}", registerDTO.getId());
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.existsByEmail(registerDTO.getEmail())) {
            log.warn("Email already exists {}", registerDTO.getEmail());
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        Member member = Member.createMember(registerDTO, passwordEncoder.encode(registerDTO.getPassword()));
        return memberRepository.save(member);
    }

}
