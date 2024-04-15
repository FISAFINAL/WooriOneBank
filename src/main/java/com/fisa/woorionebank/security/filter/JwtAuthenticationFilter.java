package com.fisa.woorionebank.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fisa.woorionebank.member.domain.dto.requestDto.LoginDto;
import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.security.TokenProvider;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final ObjectMapper  om = new ObjectMapper();
    private final AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(TokenProvider tokenProvider, AuthenticationManager authenticationManager) {
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/member/login");
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("--JWT AUTHENTICATION FILTER--");
        LoginDto loginDto = om.readValue(request.getReader(), LoginDto.class);
        String loginId = loginDto.getId();
        String pwd = loginDto.getPassword();
        log.info("authentication login id: {}", loginId);
        if (loginId.isBlank() || loginId.isEmpty() || pwd.isEmpty() || pwd.isBlank()){
            throw new BadCredentialsException("회원 아이디와 비밀번호를 다시 확인해주세요. 빈값은 넣을 수 없습니다.");
        }
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginId, pwd);
        return getAuthenticationManager().authenticate(authenticationToken);
    }

    /***
     * 인증 성공 시 JWT 토큰 발급
     * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException, IOException {
        log.info("authentication name = {}", authentication.getName());
        Member member = (Member) authentication.getPrincipal();

        String jwtToken = tokenProvider.generateToken(member);
        response.addHeader("token", jwtToken);
        response.addHeader("memberId", member.getMemberId().toString());
    }
}
