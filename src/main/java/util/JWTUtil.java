package util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;

public class JWTUtil {
    
    // IMPORTANTE: En producción, esta clave debe estar en variables de entorno
    private static final String SECRET_KEY = "mi_clave_secreta_super_segura_para_jwt_con_256_bits_minimo_requerido_12345";
    private static final long EXPIRATION_TIME = 7 * 24 * 60 * 60 * 1000; // 7 días en milisegundos
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    /**
     * Genera un token JWT para un usuario
     * @param usuarioId ID del usuario
     * @param email Email del usuario
     * @param rol Rol del usuario
     * @return Token JWT como String
     */
    public static String generateToken(int usuarioId, String email, String rol) {
        return Jwts.builder()
                .subject(String.valueOf(usuarioId))
                .claim("email", email)
                .claim("rol", rol)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }
    
    /**
     * Valida un token JWT y devuelve los claims
     * @param token Token JWT
     * @return Claims del token
     * @throws RuntimeException si el token es inválido o expirado
     */
    public static Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token expirado", e);
        } catch (JwtException e) {
            throw new RuntimeException("Token inválido", e);
        }
    }
    
    /**
     * Obtiene el ID del usuario desde el token
     */
    public static int getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return Integer.parseInt(claims.getSubject());
    }
    
    /**
     * Obtiene el email del usuario desde el token
     */
    public static String getEmailFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("email", String.class);
    }
    
    /**
     * Obtiene el rol del usuario desde el token
     */
    public static String getRolFromToken(String token) {
        Claims claims = validateToken(token);
        return claims.get("rol", String.class);
    }
    
    /**
     * Verifica si un token es válido (no expirado y bien formado)
     */
    public static boolean isTokenValid(String token) {
        try {
            validateToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}