package com.mdm.user.management.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import static io.jsonwebtoken.Jwts.*;

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


    public String generateToken(String username){
         //Map<String, Object> claims = Jwts.claims().setSubject(user.getName());
       // claims.put(AUTHORITIES_KEY, user.getRole());
//claims.put();

//        String k =  Jwts.builder()
//                .claim("subject", user.getName())
//                .claim("Role_Keys", user.getRole())
//                .setId(user.getId().toString())
//                 .setSubject(user.getUsername())
//                 .setIssuedAt(new Date(System.currentTimeMillis()))
//                 .setExpiration(new Date(System.currentTimeMillis()+60*60*10))
//                 .signWith(getSignInKey())
//                 .compact();
//        return k;

      //  UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() +  60 * 60 * 30))
                .signWith(getSignInKey())
                .compact();
    }

//    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails){
//       return Jwts
//                .builder()
//               .setClaims(extraClaims)
//               .setSubject(userDetails.getUsername())
//               .setIssuedAt(new Date())
//               .setExpiration(new Date(1000*60*5))
//               .signWith(getSignInKey(), SignatureAlgorithm.NONE)
//               .compact();
//
//    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claim = extractAllClaims(token);
        return claimsResolver.apply(claim);
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
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


    public boolean validateToken(String token, UserDetails userDtls){
        String username = extractUserName(token);
        return (username.equals(userDtls.getUsername()) && !isTokenExpired(token));
    }
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

}
