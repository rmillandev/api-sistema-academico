package com.app.sistemaacademico.repository;

import com.app.sistemaacademico.model.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProfessorRepository extends JpaRepository<Professor, Integer> {

    @Query(value = "SELECT professor.dni, professor.full_name, professor.gender, professor.phone, professor.address, \n" +
            "users.username, users.role\n" +
            "FROM professor\n" +
            "INNER JOIN users ON users.dni = professor.dni\n" +
            "WHERE professor.dni = :dni", nativeQuery = true)
    List<Object[]> getInformationProfessor(@Param("dni") long dni);

    @Query(value = "SELECT student.dni, student.full_name, student.gender, student.address, student.phone\n" +
            "FROM inscription\n" +
            "INNER JOIN course ON inscription.course_id = course.id\n" +
            "INNER JOIN student ON inscription.student_id = student.id\n" +
            "INNER JOIN professor ON course.professor_id = professor.id\n" +
            "WHERE professor.dni = :dni", nativeQuery = true)
    Page<Object[]> getStudentsFromCourses(@Param("dni") long dni, Pageable pageable);

}
