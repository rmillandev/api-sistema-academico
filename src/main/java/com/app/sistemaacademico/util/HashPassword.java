package com.app.sistemaacademico.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HashPassword {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String encriptarPassword(String password){
        return passwordEncoder.encode(password);
    }

}
