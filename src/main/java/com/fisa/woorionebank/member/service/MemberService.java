package com.fisa.woorionebank.member.service;

import com.fisa.woorionebank.member.domain.dto.MemberDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

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

}
