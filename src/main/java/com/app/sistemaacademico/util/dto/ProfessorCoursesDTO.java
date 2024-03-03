package com.app.sistemaacademico.util.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProfessorCoursesDTO {

    private int id;

    private String name;

    private String description;

    private String duration;

    private String professor;

}
