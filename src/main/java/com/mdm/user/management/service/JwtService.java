package com.mdm.user.management.service;

import com.mdm.user.management.entities.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private final String SECRET_KEY;

    public JwtService() throws NoSuchAlgorithmException {
       try {
           KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA256");
           SECRET_KEY = Base64.getEncoder().encodeToString(keyGen.generateKey().getEncoded());
       } catch (Exception e) {
           throw new RuntimeException(e);
       }

    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String generateToken(AppUser user){
         Map<String, Object> claims = new HashMap<>();
         return Jwts.builder().setClaims(claims)
                 .setSubject(user.getUsername())
                 .setIssuedAt(new Date(System.currentTimeMillis()))
                 .setExpiration(new Date(System.currentTimeMillis()+60*60*10))
                 .signWith(getSignInKey()).compact();
    }

    public boolean isTokenValid(String token, UserDetails userDtls){
        String username = extractUserName(token);
        return (username.equals(userDtls.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
       return Jwts
                .builder()
               .setClaims(extraClaims)
               .setSubject(userDetails.getUsername())
               .setIssuedAt(new Date())
               .setExpiration(new Date(1000*60*5))
               .signWith(getSignInKey(), SignatureAlgorithm.NONE)
               .compact();

    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claim = extractAllClaims(token);
        return claimsResolver.apply(claim);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
