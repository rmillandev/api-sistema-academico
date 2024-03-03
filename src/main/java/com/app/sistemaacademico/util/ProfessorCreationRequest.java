package com.app.sistemaacademico.util;

import com.app.sistemaacademico.model.Professor;
import com.app.sistemaacademico.model.Users;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProfessorCreationRequest {

    private Users user;

    private Professor professor;

}
