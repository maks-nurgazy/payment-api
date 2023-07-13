package com.maksnurgazy.util;

import com.maksnurgazy.entities.Token;
import com.maksnurgazy.model.EntryValue;
import com.maksnurgazy.model.JwtResponse;
import com.maksnurgazy.services.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {

    @Serial
    private static final long serialVersionUID = -2550185165626007488L;

    private final TokenService tokenService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.token.expiration.in.seconds}")
    private Long jwtTokenExpiration;

    //retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenExists(String token){
        return tokenService.isTokenExists(token);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public JwtResponse generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername());
    }

    private JwtResponse doGenerateToken(Map<String, Object> claims, String subject) {
        Date dateExpiration = calculateExpirationDate(new Date());
        String jwt = Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(dateExpiration)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();

        return getTokenResponse(new EntryValue<>(jwt, dateExpiration), subject);
    }

    private Date calculateExpirationDate(Date createdDate) {
        return new Date(createdDate.getTime() + jwtTokenExpiration * 1000);
    }

    //validate token
    public Boolean validateToken(String token, UserDetails userDetails) {

        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isTokenExists(token));
    }

    public JwtResponse getTokenResponse(EntryValue<String, Date> value, String username) {

        LocalDateTime expired = DateUtil.convertToLocalDateTimeViaMillisecond(value.v());

        Optional<Token> optional = tokenService.findById(value.k());

        Token token = optional.orElseGet(() -> {
            Token t = new Token();
            t.setId(value.k());

            return t;
        });

        token.setExpired(expired);
        token.setType("Bearer");
        token.setUsername(username);
        token.setCreatedDate(LocalDateTime.now());

        tokenService.save(token);

        return JwtResponse.builder()
                .type("Bearer")
                .token(value.k())
                .expiryDate(expired)
                .currentTime(LocalDateTime.now())
                .username(username).build();
    }
}
