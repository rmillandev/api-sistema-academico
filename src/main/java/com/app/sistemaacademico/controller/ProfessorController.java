package com.app.sistemaacademico.controller;

import com.app.sistemaacademico.model.Inscription;
import com.app.sistemaacademico.model.Professor;
import com.app.sistemaacademico.model.Users;
import com.app.sistemaacademico.service.ProfessorService;
import com.app.sistemaacademico.util.ProfessorCreationRequest;
import com.app.sistemaacademico.util.dto.ProfessorCoursesDTO;
import com.app.sistemaacademico.util.dto.ProfessorStudentCredentialDTO;
import com.app.sistemaacademico.util.dto.StudentFromCoursesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService service;


    @GetMapping(value = "/information")
    public List<ProfessorStudentCredentialDTO> getInformationProfessor(@RequestParam long dni) throws Exception {
        return service.getInformationProfessor(dni);
    }

    @GetMapping(value = "/courses")
    public List<ProfessorCoursesDTO> getProfessorCourses(@RequestParam long dni) throws Exception {
        return service.getProfessorCourses(dni);
    }

    @PostMapping(value = "/inscription-student")
    public ResponseEntity<Object> enrollStudent(@RequestBody Inscription inscription){
        return service.enrollStudent(inscription);
    }

    @GetMapping(value = "/students-courses")
    public Page<StudentFromCoursesDTO> getStudentsFromCourses(@RequestParam long dni, Pageable pageable) throws Exception {
        return service.getStudentsFromCourses(dni, pageable);
    }

    @PutMapping(value = "/update-information/{id}")
    public ResponseEntity<Object> updateInformation(@PathVariable int id, @RequestBody ProfessorCreationRequest request){
        Professor professor = request.getProfessor();
        Users user = request.getUser();

        return service.updateInformation(id, professor, user);
    }

    @PutMapping(value = "/update-password/{id}")
    public ResponseEntity<Object> updatePassword(@PathVariable int id, @RequestBody String password){
        return service.updatePassword(id, password);
    }

}
