package com.springboot.gateway.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AuthenticationManagerJwt implements ReactiveAuthenticationManager {

    /**
     * SpringCloud- gateway

     */
//   llaveJwt.getBytes
//    @Value("${config.security.oauth.jwt.key}")
     private String llaveJwt =  "123445jkdjsdksdjaskjdlkajdadjasddisinid90sd9js9d9sdcsdjc9sdasd8j9as8d98sdjc98sdjc9asdjsdsdsdsdwewwwwwwwwwwwwwwwwwwwwewsd98sadjsd9sd9d9d9s9ds9asasasasasd9d";

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.just(authentication.getCredentials().toString())
                .map(token->{
                    SecretKey llave = Keys.hmacShaKeyFor(Base64.getEncoder().encode(llaveJwt.getBytes()));
                    return Jwts.parserBuilder().setSigningKey(llave).build().parseClaimsJws(token).getBody();
                })
                .map(claims -> {
                    String username = claims.get("user_name",String.class);
                    List<String> roles=claims.get("authorities", List.class);
                    Collection<GrantedAuthority> authoritys = roles.stream().map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    return new UsernamePasswordAuthenticationToken(username, null,authoritys);
                });
    }
}
