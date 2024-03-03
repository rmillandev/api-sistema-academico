package com.app.sistemaacademico.util;

import com.app.sistemaacademico.model.Student;
import com.app.sistemaacademico.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StudentCreationRequest {

    private Users user;

    private Student student;

}
