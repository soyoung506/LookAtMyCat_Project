package com.hanghae.lookAtMyCat.security.provider;

import com.hanghae.lookAtMyCat.security.token.JwtAuthenticationToken;
import com.hanghae.lookAtMyCat.security.util.JwtTokenizer;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenizer jwtTokenizer;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;
        Claims claims = jwtTokenizer.parseAccessToken(authenticationToken.getToken());
        // 보안을 위해 subject에 암호화된 데이터를 저장하고, 해당 코드에 복호화하는 과정을 넣을 수 있다.
//        String userEmail = claims.getSubject();
        Object userKey = claims.get("userKey");
        return new JwtAuthenticationToken(null, userKey, null);
    }

    private List<GrantedAuthority> getGrantedAuthorities(Claims claims) {
        List<String> roles = (List<String>) claims.get("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(()-> role);
        }
        return authorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
