package com.example.democrm.service;

import com.example.democrm.constant.ErrorCodeDefs;
import com.example.democrm.exception.JwtTokenInvalid;
import com.example.democrm.model.UserDetailsImpl;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtTokenProvider {
    @Value("${jwt.jwtSecret}")
    private String jwtSecret;
    @Value("${jwt.jwtExpirationMs}")
    private Long jwtExpirationMs;
    @Value("${jwt.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationsMs;
    private  static final String AUTHORITIES_KEY = "AUTHOR";

    //ma hoa jwtsecret
    @PostConstruct
    public void init(){
        jwtSecret = Base64.getEncoder().encodeToString(jwtSecret.getBytes());
        log.info("jwt secret: {}", jwtSecret);
    }

    //gen token ung voi quyen
    public String generateTokenWithAuthorities(Authentication authentication){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs); //ngay het han
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();//getPrincipal(): Trả về người dùng được xác thực
        final String authorities = authentication.getAuthorities().stream()//lay danh sach cac quyen
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(",")); //joining ghep cac quyen thanh mot chuoi va ngan cach voi nhau bang dau phay

        return Jwts.builder()   //tao jwts
                .setSubject(userPrincipal.getUsername())
                .claim(AUTHORITIES_KEY, authorities)        //ds cac quyen
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public UsernamePasswordAuthenticationToken getUserInfoFromJWT(String token){        //giải mã JWT và trích xuất thông tin người dùng từ JWT.
        Claims claims = Jwts.parser()   //Jwts.parser() : tao doi tuong de giai ma jwt
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)  //giải mã JWT và lấy ra đối tượng Claims chứa thông tin người dùng.
                .getBody();
        if(claims.get(AUTHORITIES_KEY) != null && claims.get(AUTHORITIES_KEY) instanceof String authoritiesStr && !Strings.isBlank(authoritiesStr)){
            Collection authorities = Arrays.stream(authoritiesStr.split(","))
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
            return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        } else {
            return new UsernamePasswordAuthenticationToken(claims.getSubject(),"", new ArrayList<>());
        }
    }

    public boolean validateJwtToken(String authToken){  //xac thuc jwttoken
        try{
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        }catch (SignatureException e){
            log.error("Invalid JWT signature: {}", e.getMessage());
        }catch (MalformedJwtException e){
            log.error("Invalid JWT token: {}", e.getMessage());
        }catch (ExpiredJwtException e){
            log.error("JWT token is expired: {}",e.getMessage());
        }catch (UnsupportedJwtException e){
            log.error("JWT token is unsupported: {}", e.getMessage());
        }catch (IllegalArgumentException e){
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        throw new JwtTokenInvalid(ErrorCodeDefs.getErrMsg(ErrorCodeDefs.TOKEN_INVALID));
    }

}









