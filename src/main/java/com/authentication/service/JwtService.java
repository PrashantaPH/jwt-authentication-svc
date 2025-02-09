package com.authentication.service;

import com.authentication.dto.TokenResponse;
import com.authentication.exception.InvalidAuthException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenResponse generateToken(String username, String password) {
        TokenResponse response = new TokenResponse();
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            if (authentication.isAuthenticated()) {
                String accessToken = generateToken(username);
                long expiresIn = calculateExpireIn(accessToken);

                String refreshToken = generateRefreshToken(new HashMap<>(), username);
                long refreshExpiresIn = calculateExpireIn(refreshToken);

                boolean active = !isTokenExpired(accessToken);

                response.setResult("SUCCESS");
                response.setActive(active);
                response.setAccessToken(accessToken);
                response.setExpiresIn(expiresIn);
                response.setRefreshToken(refreshToken);
                response.setRefreshExpiresIn(refreshExpiresIn);
                return response;
            } else {
                throw new InvalidAuthException("User is not authorized.");
            }
        } catch (BadCredentialsException ex) {
            throw new BadCredentialsException("Invalid credentials provided for user: " + username);
        } catch (InvalidAuthException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException("Technical error accord");
        }
    }

    public TokenResponse validateToken(String accessToken) {
        TokenResponse response = new TokenResponse();
        try {
            String username = extractUsername(accessToken);
            boolean isExpired = isTokenExpired(accessToken);

            response.setResult(isExpired ? "EXPIRED": "VALID");
            response.setActive(!isExpired);
            response.setAccessToken(accessToken);
            response.setExpiresIn(calculateExpireIn(accessToken));
        } catch (Exception e) {
            throw new InvalidAuthException("Token validation failed");
        }
        return response;
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public  <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        final Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public long calculateExpireIn(String token) {
        Date expiration = extractExpiration(token);
        long expiresInMillis = expiration.getTime() - System.currentTimeMillis();
        return expiresInMillis / 1000;
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignedKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Map<String, Object> claims, String username) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 604800000))
                .signWith(getSignedKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignedKey() {
        byte[] keyBytes = Decoders.BASE64.decode("6DdeonFf4OyTTSzS76nSzmlOjvusBkCjvtgRB0lcvR4");
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
