package com.example.democrm.security;

import com.example.democrm.service.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class AuthTokenFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AuthTokenFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {//kiem tra trong header có chua JWT ko va xac thuc no
        try {
            String jwt = parseJwt(request); //lay ra token tu header cua request
            if (StringUtils.hasText(jwt) && jwtTokenProvider.validateJwtToken(jwt)) {     //kiem tra token co hop le ko
                UsernamePasswordAuthenticationToken authenticationToken = jwtTokenProvider.getUserInfoFromJWT(jwt); //token hop le thi lay ra thong tin
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);  //thiet lap lai thong tin, sử dụng SecurityContextHolder để lưu trữ thông tin xác thực của người dùng vào SecurityContext
            }
        } catch (Exception ex) {
            throw ex;
        }
        filterChain.doFilter(request, response);    //cho phep request tiep tuc dc xu lý,
    }

    private String parseJwt(HttpServletRequest request) {    //lay Jwt từ header
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); //trả về chuỗi token và cắt bỏ 7 ký tự đầu tiên là "Bearer ",(lấy token sau chuỗi "Bearer ")
        }
        return null;
    }
}
