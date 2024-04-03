package com.fisa.woorionebank.member.controller;

import com.fisa.woorionebank.config.Header;
import com.fisa.woorionebank.member.domain.dto.MemberDTO;
import com.fisa.woorionebank.member.domain.dto.ResponseDTO;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.service.MemberService;
import com.fisa.woorionebank.security.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "예제 API", description = "Swagger 테스트용 API")
public class MemberController {

    private final MemberService memberService;

    private final TokenProvider tokenProvider;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping(value = "/user")
    @Parameter(name = "gettest", description = "getAPI")
    @Operation(summary = "유저 저장", description = "유저 정보를 DTO로 전송하고 DTO로 반환합니다.")
    public Header<MemberDTO> createMember(@RequestBody MemberDTO memberDTO){
        log.info("{요청 확인}");
        return Header.description(memberService.createMember(memberDTO), "유저 저장 완료");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody MemberDTO memberDTO) {
        try {

            Member member = Member.builder()
                    .memberId(memberDTO.getMemberId())
                    .loginId(memberDTO.getId())
                    .email(memberDTO.getEmail())
                    .name(memberDTO.getName())
                    .password(passwordEncoder.encode(memberDTO.getPassword()))
                    .age(memberDTO.getAge())
                    .build();

            Member registeredMember = memberService.create(member);

            return ResponseEntity.ok().body(registeredMember);
        }
        catch (Exception e) {
            ResponseDTO responseDTO = ResponseDTO.builder().error(e.getMessage()).build();

            return ResponseEntity
                    .internalServerError() // Error 500
                    .body(responseDTO);
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticate(@RequestBody MemberDTO memberDTO) {
        Member member = memberService.getByCredentials(
                memberDTO.getId(),
                memberDTO.getPassword(),
                passwordEncoder);

        if(member != null) {
            // 토큰 생성
            final String token = tokenProvider.create(member);

            final MemberDTO responseMemberDTO = MemberDTO.builder()
                    .memberId(member.getMemberId())
                    .id(member.getLoginId())
                    .password(member.getPassword())
                    .name(member.getName())
                    .age(member.getAge())
                    .email(member.getEmail())
                    .grade(member.getGrade())
                    .saving(member.getSavings())
                    .accounts(member.getAccounts())
                    .token(token)
                    .build();

            return ResponseEntity.ok().body(member.createMember(responseMemberDTO));
        }
        else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Login failed.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    @PostMapping("/signout")
    public ResponseEntity<?> signOut(@AuthenticationPrincipal Member member) {
        if (member != null) {
            return ResponseEntity.ok().body("Logout Succeed.");
        }
        else {
            ResponseDTO responseDTO = ResponseDTO.builder()
                    .error("Logout failed.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseDTO);
        }
    }

    @PostMapping("/checkid")
    public ResponseEntity<?> checkUserId(@RequestBody MemberDTO memberDTO) {
        Boolean isUserIdVerified = memberService.checkUserId(memberDTO.getId());

        if (isUserIdVerified) {
                MemberDTO responseUserDTO = MemberDTO.builder()
                    .isVerified(true)
                    .build();
            return ResponseEntity.ok().body(responseUserDTO);
        }
        else {
            ResponseDTO responseCheckUserIdDTO = ResponseDTO.builder()
                    .error("아이디가 중복됩니다.")
                    .build();

            return ResponseEntity
                    .internalServerError()
                    .body(responseCheckUserIdDTO);
        }
    }
}
