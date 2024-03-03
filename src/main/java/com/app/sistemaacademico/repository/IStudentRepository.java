package com.app.sistemaacademico.repository;

import com.app.sistemaacademico.model.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Integer> {

    @Query(value = "SELECT student.dni, student.full_name, student.gender, student.phone, student.address, \n" +
            "users.username, users.role\n" +
            "FROM student\n" +
            "INNER JOIN users ON users.dni = student.dni\n" +
            "WHERE student.dni = :dni", nativeQuery = true)
    List<Object[]> getInformationStudent(@Param("dni") long dni);

    @Query(value = "SELECT course.name, course.description, course.duration, professor.full_name\n" +
            "FROM inscription\n" +
            "INNER JOIN course ON inscription.course_id = course.id\n" +
            "INNER JOIN student ON inscription.student_id = student.id\n" +
            "INNER JOIN professor ON course.professor_id = professor.id\n" +
            "WHERE student.dni = :dni", nativeQuery = true)
    Page<Object[]> getCoursesFromStudent(@Param("dni") long dni, Pageable pageable);

}
