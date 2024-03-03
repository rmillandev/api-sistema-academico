package com.app.sistemaacademico.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentFromCoursesDTO {

    private long dni;

    private String full_name;

    private String gender;

    private String address;

    private String phone;

}
