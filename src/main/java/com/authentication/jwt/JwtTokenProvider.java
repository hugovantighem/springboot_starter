package com.authentication.jwt;

import com.authentication.CustomUserPrincipal;
import com.settings.JwtSettings;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private JwtSettings settings;

    public String extractJwt(String bearerToken) {
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(settings.getTokenType())) {
            return bearerToken.substring(settings.getTokenType().length() + 1);
        }
        return null;
    }

    public String generateToken(Authentication authentication) throws UnsupportedEncodingException {

        CustomUserPrincipal userPrincipal = (CustomUserPrincipal) authentication.getPrincipal();
        String roles = getUserRoles(userPrincipal);


        LocalDateTime now = LocalDateTime.now();
        Date issuedAt = Date.from(now.atZone(ZoneId.systemDefault()).toInstant());
        LocalDateTime ldt = now.plusMinutes(settings.getJwtExpirationInMinutes());
        Date expirationDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .setIssuer(settings.getAuthServiceJwt())
                .setIssuedAt(issuedAt)
                .setExpiration(expirationDate)
                .claim("roles", roles)
                .signWith(SignatureAlgorithm.forName(settings.getAlg()), settings.getSecret().getBytes("UTF-8"))
                .compact();
    }
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(settings.getSecret().getBytes("UTF-8")).parseClaimsJws(authToken);
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
                .setSigningKey(settings.getSecret().getBytes("UTF-8"))
                .parseClaimsJws(jwt).getBody();
        return claims.getSubject();
    }
    private String getUserRoles(UserDetails user) {
        return String.join(",", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }

    public String authorizationToken(String jwt) {
        return settings.getTokenType() + " " + jwt;
    }

    public String headerKey() {
        return settings.getAuthorization();
    }
}
