package com.fisa.woorionebank.member.service;

import com.fisa.woorionebank.member.domain.dto.MemberDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ModelMapper mapper = new ModelMapper();

    @Transactional
    public MemberDTO createMember(MemberDTO memberDTO){
        Member member = Member.createMember(memberDTO);

        Member save = memberRepository.save(member);
        MemberDTO dto = mapper.map(save, MemberDTO.class);
        return dto;
    }


    public Member create(final Member member) {
        if (member == null || member.getLoginId() == null || member.getEmail() == null) {
            throw new RuntimeException("Invalid arguments");
        }

        final String loginId = member.getLoginId();
        final String email = member.getEmail();

        if (memberRepository.existsByLoginId(loginId)) {
            log.warn("UserId already exists {}", loginId);
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }

        if (memberRepository.existsByEmail(email)) {
            log.warn("Email already exists {}", email);
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }

        return memberRepository.save(member);
    }

    public Member getByCredentials(final String loginId, final String password, final PasswordEncoder encoder) {
        final Member originalUser = memberRepository.findByLoginId(loginId);

        if (originalUser != null && encoder.matches(password, originalUser.getPassword())) return originalUser;

        return null;
    }

    public Boolean checkUserId(String loginId) {
        return !memberRepository.existsByLoginId(loginId);
    }
}
