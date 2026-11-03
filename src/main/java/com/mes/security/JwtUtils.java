package com.mes.security;

import com.mes.entity.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms:86400000}")
    private int jwtExpirationMs;

    private SecretKey getSigningKey() {
        try {
            // 先尝试Base64解码
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            logger.info("使用Base64解码的JWT密钥，长度: {}", keyBytes.length);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (IllegalArgumentException e) {
            // 如果不是Base64，使用原始字符串
            logger.warn("JWT密钥不是Base64编码，使用原始字符串");
            byte[] keyBytes = jwtSecret.getBytes();
            if (keyBytes.length < 32) {
                logger.error("JWT密钥太短({}字节)，需要至少32字节。请配置app.jwt.secret为Base64编码的长密钥", keyBytes.length);
                // 生产环境应该抛出异常，开发环境可以生成临时密钥
                logger.warn("生成临时安全密钥，重启后所有token将失效");
                return Keys.secretKeyFor(SignatureAlgorithm.HS256);
            }
            return Keys.hmacShaKeyFor(keyBytes);
        }
    }

    public String generateJwtToken(User user) {
        return generateTokenFromUsername(user.getUsername());
    }

    public String generateTokenFromUsername(String username) {
        SecretKey key = getSigningKey();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            SecretKey key = getSigningKey();
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            logger.warn("JWT令牌已过期: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("解析JWT令牌失败: {}", e.getMessage());
            throw e;
        }
    }

    public boolean validateJwtToken(String authToken) {
        try {
            SecretKey key = getSigningKey();
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(authToken);
            return true;
        } catch (SecurityException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }
}