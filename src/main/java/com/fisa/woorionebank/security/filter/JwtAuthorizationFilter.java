package com.fisa.woorionebank.security.filter;

import com.fisa.woorionebank.security.TokenProvider;
import com.fisa.woorionebank.security.service.JwtDetailsService;
import com.fisa.woorionebank.security.user.PrincipalDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private final TokenProvider tokenProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider, JwtDetailsService jwtDetailsService) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader(TokenProvider.HEADER_STRING);
            log.info("JWT 토큰 검증 필터");

            //회원 가입이면 필터 바로 통과
            String contextPath = request.getServletPath();
            if(contextPath.equals("/api/member/signup")){
                filterChain.doFilter(request, response);
            } else {
                if(authorizationHeader != null && authorizationHeader.startsWith(TokenProvider.TOKEN_PREFIX)) {
                    //Bearer 자르기
                    String token = authorizationHeader.substring("Bearer ".length());
                    String loginId = tokenProvider.validateAndGetMemberId(token);

                    Authentication authentication = tokenProvider.authenticateToken(loginId);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    filterChain.doFilter(request, response);
                }
            }
        }
        catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
    }
}
