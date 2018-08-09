package com.authentication.jwt;

import com.authentication.CustomUserPrincipal;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {


    // TODO externalize
    private static final SignatureAlgorithm alg = SignatureAlgorithm.HS256;
    public static final String SECRET = "secret";
    private int jwtExpirationInMinutes = 10080; // 7 days
    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_TYPE = "Bearer";
    public static final String AUTH_SERVICE_JWT = "auth-service-jwt";

    public static String extractJwt(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtTokenProvider.TOKEN_TYPE)) {
            return bearerToken.substring(JwtTokenProvider.TOKEN_TYPE.length() + 1);
        }
        return null;
    }

    public String generateToken(Authentication authentication) throws UnsupportedEncodingException {

        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        String roles = getUserRoles(userPrincipal);


        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime ldt = now.plusMinutes(jwtExpirationInMinutes);
        Date expirationDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuer(AUTH_SERVICE_JWT)
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .claim("roles", roles)
                .signWith(alg, SECRET.getBytes("UTF-8"))
                .compact();
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(SECRET.getBytes("UTF-8")).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncodingException.");
        }
        return false;
    }

    public String getUsernameFromJWT(String jwt) throws UnsupportedEncodingException {
        Claims claims = Jwts.parser()
                .setSigningKey(SECRET.getBytes("UTF-8"))
                .parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }
    private String getUserRoles(UserDetails user) {
        return String.join(",", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    public static String authorizationToken(String jwt) {
        return TOKEN_TYPE + " " + jwt;
    }
}
