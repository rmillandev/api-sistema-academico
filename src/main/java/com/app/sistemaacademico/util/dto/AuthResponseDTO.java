package com.app.sistemaacademico.util.dto;

// Esta clase devolvera la informacion con el token y el tipo que tenga este

import lombok.Data;

@Data
public class AuthResponseDTO {
    
    private String accessToken;
    
    private String tokenType = "Bearer ";

    public AuthResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }
    
    
}
