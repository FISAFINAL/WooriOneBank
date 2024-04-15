package com.fisa.woorionebank.security;

import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.security.service.JwtDetailsService;
import com.fisa.woorionebank.security.user.PrincipalDetails;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.time.Duration;

@Slf4j
@Service
public class TokenProvider implements AuthenticationProvider {

    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    private static final String SECRET_KEY = "rkGU45258GGhiolLO2465TFY5345kGU45258GGhiolLO2465TFY5345";
    private static final long TOKEN_EXPIRE_TIME = Duration.ofDays(14).toMillis(); //14일
    private final JwtDetailsService jwtDetailsService;

    public TokenProvider(JwtDetailsService jwtDetailsService) {
        this.jwtDetailsService = jwtDetailsService;
    }

    private Key getSecretKey(String secretKey) {
        byte[] KeyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(KeyBytes);
    }

    public String generateToken(Member member) {
        return Jwts.builder()
                .setSubject(member.getLoginId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + TOKEN_EXPIRE_TIME))
                .signWith(getSecretKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateAndGetMemberId(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY.getBytes())
                .build()
                .parseClaimsJws(token).getBody()
                .getSubject();
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String id = (String) authentication.getPrincipal();
        PrincipalDetails member = (PrincipalDetails)jwtDetailsService.loadUserByUsername(id);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                member.getMember(),
                null,
                AuthorityUtils.NO_AUTHORITIES
        );
        return authenticationToken;
    }

    public Authentication authenticateToken(String id) throws AuthenticationException {

        PrincipalDetails member = (PrincipalDetails)jwtDetailsService.loadUserByUsername(id);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                member.getMember(),
                null,
                AuthorityUtils.NO_AUTHORITIES
        );
        return authenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
