package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utilidad para hashear y verificar contraseñas
 * Usa SHA-256 con salt para mayor seguridad
 */
public class PasswordUtil {
    
    private static final int SALT_LENGTH = 16;
    
    /**
     * Hashea una contraseña con salt
     * @param plainPassword Contraseña en texto plano
     * @return String con formato "salt:hash" en Base64
     */
    public static String hashPassword(String plainPassword) {
        try {
            // Generar salt aleatorio
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Hashear la contraseña con el salt
            String hash = hashWithSalt(plainPassword, salt);
            
            // Devolver "salt:hash" en Base64
            return Base64.getEncoder().encodeToString(salt) + ":" + hash;
        } catch (Exception e) {
            throw new RuntimeException("Error al hashear contraseña", e);
        }
    }
    
    /**
     * Verifica si una contraseña coincide con su hash
     * @param plainPassword Contraseña en texto plano
     * @param storedPassword Hash almacenado en formato "salt:hash"
     * @return true si coincide, false si no
     */
    public static boolean checkPassword(String plainPassword, String storedPassword) {
        try {
            // Separar salt y hash
            String[] parts = storedPassword.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            String storedHash = parts[1];
            
            // Hashear la contraseña ingresada con el mismo salt
            String computedHash = hashWithSalt(plainPassword, salt);
            
            // Comparar hashes
            return storedHash.equals(computedHash);
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Hashea una contraseña con un salt específico
     */
    private static String hashWithSalt(String password, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes());
        return Base64.getEncoder().encodeToString(hashedPassword);
    }
}