package com.fisa.woorionebank.security;

import com.fisa.woorionebank.member.entity.Member;
import com.fisa.woorionebank.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private MemberRepository memberRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authorizationHeader = request.getHeader(TokenProvider.HEADER_STRING);
            log.info("Filter is running...");

            if(authorizationHeader != null && authorizationHeader.startsWith(TokenProvider.TOKEN_PREFIX)) {
                //Bearer 자르기
                String token = authorizationHeader.substring("Bearer ".length());
                String loginId = tokenProvider.validateAndGetUserId(token);
                log.info("Authenticated loginId : " + loginId);

                Member member = memberRepo.findByLoginId(loginId);

                AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        member,
                        null,
                        AuthorityUtils.NO_AUTHORITIES
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
                securityContext.setAuthentication(authentication);
                SecurityContextHolder.setContext(securityContext);
            }
        }
        catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }
        filterChain.doFilter(request, response);
    }
}
