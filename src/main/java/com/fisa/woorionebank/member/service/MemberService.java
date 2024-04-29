package com.fisa.woorionebank.member.service;

import com.fisa.woorionebank.common.execption.CustomException;
import com.fisa.woorionebank.common.execption.ErrorCode;
import com.fisa.woorionebank.member.domain.dto.request.LoginDto;
import com.fisa.woorionebank.member.domain.dto.request.RegisterDTO;
import com.fisa.woorionebank.member.domain.dto.response.MemberDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Transactional
    public MemberDTO createMember(RegisterDTO registerDTO) {

        if (memberRepository.existsByLoginId(registerDTO.getId())) {
            throw new CustomException(ErrorCode.DUPLICATE_MEMBER);
        }

        if (memberRepository.existsByEmail(registerDTO.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }

        Member member = Member.createMember(registerDTO, passwordEncoder.encode(registerDTO.getPassword()));

        Member savedMember = memberRepository.save(member);


        return MemberDTO.fromEntity(savedMember);
    }

    @Transactional
    public MemberDTO login(LoginDto loginDto) {

        Member member = memberRepository.findByLoginId(loginDto.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_Member_Login));

        if (!passwordEncoder.matches(loginDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_Member_Password);
        }

        return MemberDTO.fromEntity(member);

    }
}
