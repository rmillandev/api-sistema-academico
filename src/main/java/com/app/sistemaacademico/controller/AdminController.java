package com.app.sistemaacademico.controller;

import com.app.sistemaacademico.model.*;
import com.app.sistemaacademico.service.AdminService;
import com.app.sistemaacademico.util.ProfessorCreationRequest;
import com.app.sistemaacademico.util.dto.StudentCoursesDTO;
import com.app.sistemaacademico.util.StudentCreationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(path = "/api/admin")
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping(value = "/get-all-students")
    public Page<Student> getAllStudents(@PageableDefault(page = 0, size = 12)Pageable pageable){
        return service.getAllStudents(pageable);
    }

    @GetMapping(value = "/get-student-by-id/{id}")
    public ResponseEntity<Object> getStudentById(@PathVariable int id){
        return service.getStudentById(id);
    }

    @PostMapping(value = "/create-student")
    public ResponseEntity<Object> createStudent(@RequestBody StudentCreationRequest request){
        Users user = request.getUser();
        Student student = request.getStudent();

        return service.createStudent(user, student);
    }

    @PutMapping(value = "/update-student/{id}")
    public ResponseEntity<Object> updateStudent(@PathVariable int id, @RequestBody StudentCreationRequest request){
        Student student = request.getStudent();
        Users user = request.getUser();

        return service.updateStudent(id, student, user);
    }

    @DeleteMapping(value = "/delete-student/{id}")
    public ResponseEntity<Object> deleteStudent(@PathVariable int id){
        return service.deleteStudent(id);
    }


    // *****************************************************


    @GetMapping(value = "/get-all-professors")
    public Page<Professor> getAllProfessors(@PageableDefault(page = 0, size = 12)Pageable pageable){
        return service.getAllProfessors(pageable);
    }

    @GetMapping(value = "/get-professor-by-id/{id}")
    public ResponseEntity<Object> getProfessorById(@PathVariable int id){
        return service.getProfessorById(id);
    }

    @PostMapping(value = "/create-professor")
    public ResponseEntity<Object> createProfessor(@RequestBody ProfessorCreationRequest request){
        Users user = request.getUser();
        Professor professor = request.getProfessor();

        return service.createProfessor(user, professor);
    }

    @PutMapping(value = "/update-professor/{id}")
    public ResponseEntity<Object> updateProfessor(@PathVariable int id, @RequestBody ProfessorCreationRequest request){
        Professor professor = request.getProfessor();
        Users user = request.getUser();

        return service.updateProfessor(id, professor, user);
    }

    @DeleteMapping(value = "/delete-professor/{id}")
    public ResponseEntity<Object> deleteProfessor(@PathVariable int id){
        return service.deleteProfessor(id);
    }


    // *****************************************************


    @GetMapping(value = "/get-all-courses")
    public Page<Course> getAllCourses(@PageableDefault(page = 0, size = 12)Pageable pageable){
        return service.getAllCourses(pageable);
    }

    @GetMapping(value = "/get-course-by-id/{id}")
    public ResponseEntity<Object> getCourseById(@PathVariable int id){
        return service.getCourseById(id);
    }

    @PostMapping(value = "/create-course")
    public ResponseEntity<Object> createCourse(@RequestBody Course course){
        return service.createCourse(course);
    }

    @PutMapping(value = "/update-course/{id}")
    public ResponseEntity<Object> updateCourse(@PathVariable int id, @RequestBody Course course){
        return service.updateCourse(id, course);
    }

    @DeleteMapping(value = "/delete-course/{id}")
    public ResponseEntity<Object> deleteCourse(@PathVariable int id){
        return service.deleteCourse(id);
    }


    // *****************************************************


    @PostMapping(value = "/inscription-student")
    public ResponseEntity<Object> enrollStudent(@RequestBody Inscription inscription){
        return service.enrollStudent(inscription);
    }

    @GetMapping(value = "/student-courses")
    public List<StudentCoursesDTO> getStudentCourses(@RequestParam long dni) throws Exception {
        return service.getStudentCourses(dni);
    }
}
