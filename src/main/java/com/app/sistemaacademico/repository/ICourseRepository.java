package com.app.sistemaacademico.repository;

import com.app.sistemaacademico.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "SELECT course.name, course.description, course.duration, professor.full_name AS professor\n" +
            "FROM inscription\n" +
            "INNER JOIN course ON inscription.course_id = course.id\n" +
            "INNER JOIN student ON inscription.student_id = student.id\n" +
            "INNER JOIN professor ON course.professor_id = professor.id\n" +
            "WHERE student.dni = :dni", nativeQuery = true)
    List<Object[]> getStudentCourses(@Param("dni") long dni);

    @Query(value = "SELECT course.id, course.name, course.description, course.duration, professor.full_name\n" +
            "FROM course\n" +
            "INNER JOIN professor ON course.professor_id = professor.id\n" +
            "WHERE professor.dni = :dni", nativeQuery = true)
    List<Object[]> getProfessorCourses(@Param("dni") long dni);

}
