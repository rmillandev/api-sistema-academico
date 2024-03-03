package com.app.sistemaacademico.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentCoursesDTO {

    private String name;

    private String description;

    private String duration;

    private String professor;

}
