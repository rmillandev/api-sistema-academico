package com.app.sistemaacademico.controller;

import com.app.sistemaacademico.model.Inscription;
import com.app.sistemaacademico.model.Student;
import com.app.sistemaacademico.model.Users;
import com.app.sistemaacademico.service.StudentService;
import com.app.sistemaacademico.util.StudentCreationRequest;
import com.app.sistemaacademico.util.dto.CoursesFromStudentDTO;
import com.app.sistemaacademico.util.dto.ProfessorStudentCredentialDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/student")
public class StudentController {

    @Autowired
    private StudentService service;


    @GetMapping(value = "/information")
    public List<ProfessorStudentCredentialDTO> getInformationStudent(@RequestParam long dni) throws Exception {
        return service.getInformationStudent(dni);
    }

    @GetMapping(value = "/courses")
    public Page<CoursesFromStudentDTO> getCoursesFromStudent(@PageableDefault(page = 0, size = 12) Pageable pageable, @RequestParam long dni) throws Exception {
        return service.getCoursesFromStudent(pageable, dni);
    }

    @PostMapping(value = "/inscription")
    public ResponseEntity<Object> inscription(@RequestBody Inscription inscription){
        return service.inscription(inscription);
    }

    @PutMapping(value = "/update-information/{id}")
    public ResponseEntity<Object> updateInformation(@PathVariable int id, @RequestBody StudentCreationRequest request){
        Users user = request.getUser();
        Student student = request.getStudent();

        return service.updateInformation(id, user, student);
    }

    @PutMapping(value = "/update-password/{id}")
    public ResponseEntity<Object> updatePassword(@PathVariable int id, @RequestBody String password){
        return service.updatePassword(id, password);
    }

}
