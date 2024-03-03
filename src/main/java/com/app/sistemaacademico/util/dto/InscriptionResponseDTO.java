package com.app.sistemaacademico.util.dto;

import com.app.sistemaacademico.model.Course;
import com.app.sistemaacademico.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class InscriptionResponseDTO {

    private int id;

    private Student student;

    private Course course;

}
