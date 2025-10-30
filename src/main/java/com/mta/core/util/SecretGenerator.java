package com.mta.core.util;

import java.security.SecureRandom;
import java.util.Base64;

public class SecretGenerator {
    
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        
        // Generate JWT Secret (32 bytes)
        byte[] jwtBytes = new byte[32];
        random.nextBytes(jwtBytes);
        String jwtSecret = Base64.getEncoder().encodeToString(jwtBytes);
        
        // Generate Token Secret (32 bytes)
        byte[] tokenBytes = new byte[32];
        random.nextBytes(tokenBytes);
        String tokenSecret = Base64.getEncoder().encodeToString(tokenBytes);
        
        System.out.println("JWT_SECRET=" + jwtSecret);
        System.out.println("TOKEN_SECRET=" + tokenSecret);
    }
}
