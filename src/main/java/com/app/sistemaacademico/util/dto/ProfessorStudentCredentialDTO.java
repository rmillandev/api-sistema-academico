package com.app.sistemaacademico.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessorStudentCredentialDTO {

    private long dni;

    private String full_name;

    private String gender;

    private String phone;

    private String address;

    private String username;

    private String role;

}
