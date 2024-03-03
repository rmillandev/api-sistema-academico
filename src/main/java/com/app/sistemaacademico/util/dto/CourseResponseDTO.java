package com.app.sistemaacademico.util.dto;

import com.app.sistemaacademico.model.Professor;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CourseResponseDTO {

    private int id;

    private String name;

    private String description;

    private String duration;

    private Professor professor;

}
