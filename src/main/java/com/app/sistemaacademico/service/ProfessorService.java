package com.app.sistemaacademico.service;

import com.app.sistemaacademico.model.*;
import com.app.sistemaacademico.repository.*;
import com.app.sistemaacademico.util.HashPassword;
import com.app.sistemaacademico.util.dto.InscriptionResponseDTO;
import com.app.sistemaacademico.util.dto.ProfessorCoursesDTO;
import com.app.sistemaacademico.util.dto.ProfessorStudentCredentialDTO;
import com.app.sistemaacademico.util.dto.StudentFromCoursesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProfessorService {

    @Autowired
    private IProfessorRepository professorRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IInscriptionRepository inscriptionRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private HashPassword hashPassword;

    HashMap<String, Object> data;


    // Obtener informacion del profesor
    public List<ProfessorStudentCredentialDTO> getInformationProfessor(long dni) throws Exception {
        try {
            return professorRepository.getInformationProfessor(dni).stream()
                    .map(result -> {
                        ProfessorStudentCredentialDTO professorCredentialDTO = new ProfessorStudentCredentialDTO();
                        professorCredentialDTO.setDni((Long) result[0]);
                        professorCredentialDTO.setFull_name((String) result[1]);
                        professorCredentialDTO.setGender((String) result[2]);
                        professorCredentialDTO.setPhone((String) result[3]);
                        professorCredentialDTO.setAddress((String) result[4]);
                        professorCredentialDTO.setUsername((String) result[5]);
                        professorCredentialDTO.setRole((String) result[6]);
                        return professorCredentialDTO;
                    }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new Exception("Error al obtener la informacion del profesor: " + ex.getMessage());
        }
    }

    // Obtener los cursos que pertenecen al profesor
    public List<ProfessorCoursesDTO> getProfessorCourses(long dni) throws Exception {
        try {
            return courseRepository.getProfessorCourses(dni).stream()
                    .map(result -> {
                        ProfessorCoursesDTO professorCoursesDTO = new ProfessorCoursesDTO();
                        professorCoursesDTO.setId((Integer) result[0]);
                        professorCoursesDTO.setName((String) result[1]);
                        professorCoursesDTO.setDescription((String) result[2]);
                        professorCoursesDTO.setDuration((String) result[3]);
                        professorCoursesDTO.setProfessor((String) result[4]);
                        return professorCoursesDTO;
                    }).collect(Collectors.toList());
        } catch (Exception ex) {
            throw new Exception("Error al obtener los cursos del profesor: " + ex.getMessage());
        }
    }

    // Inscribir estudiantes a sus cursos
    public ResponseEntity<Object> enrollStudent(Inscription inscription) {
        data = new HashMap<>();

        try {
            // Se guarda la inscripcion
            Inscription savedInscription = inscriptionRepository.save(inscription);

            // Se crea una instancia de Inscription DTO
            InscriptionResponseDTO inscriptionResponseDTO = new InscriptionResponseDTO();
            inscriptionResponseDTO.setId(savedInscription.getId());

            // Se busca el estudiante por id
            Student studentId = savedInscription.getStudent();
            Optional<Student> student = studentRepository.findById(studentId.getId());

            // Se verifica si existe y se  asigna el estudiante a la DTO
            // Usar :: simplifica el metodo, si existe un valor este se agregar a la DTO
            student.ifPresent(inscriptionResponseDTO::setStudent);

            // Se busca el curso por id
            Course courseId = savedInscription.getCourse();
            Optional<Course> course = courseRepository.findById(courseId.getId());

            // Se verifica si existe y se  asigna el curso a la DTO
            // Usar :: simplifica el metodo, si existe un valor este se agregar a la DTO
            course.ifPresent(inscriptionResponseDTO::setCourse);

            data.put("message", "Estudiante inscrito correctamente");
            data.put("body", inscriptionResponseDTO);

            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (Exception ex) {
            data.put("error", "No se pudo inscribir al estudiante al curso: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener estudiantes de sus cursos
    public Page<StudentFromCoursesDTO> getStudentsFromCourses(long dni, Pageable pageable) throws Exception {
        try {
            // Consulta paginada
            Page<Object[]> resultsPage = professorRepository.getStudentsFromCourses(dni, pageable);

            // Se mapea el resultado a DTO
            return resultsPage.map(result -> {
                StudentFromCoursesDTO studentFromCoursesDTO = new StudentFromCoursesDTO();
                studentFromCoursesDTO.setDni((Long) result[0]);
                studentFromCoursesDTO.setFull_name((String) result[1]);
                studentFromCoursesDTO.setGender((String) result[2]);
                studentFromCoursesDTO.setAddress((String) result[3]);
                studentFromCoursesDTO.setPhone((String) result[4]);
                return studentFromCoursesDTO;
            });
        } catch (Exception ex) {
            throw new Exception("Error al obtener los estudiantes que pertenecen a los cursos del profesor: " + ex.getMessage());
        }
    }

    // Actualizar informacion del profesor
    public ResponseEntity<Object> updateInformation(int id, Professor professor, Users user) {
        data = new HashMap<>();

        try {
            // Verificar si el ID es menor o igual a 0, si es asi lanza un error
            if (id <= 0) {
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }

            // Se busca al profesor por el ID
            Optional<Professor> result = professorRepository.findById(id);

            // Se verifica si existe
            if (!result.isPresent()) {
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }

            Professor professorUpdate = result.get();
            professorUpdate.setFull_name(professor.getFull_name());
            professorUpdate.setDni(professor.getDni());
            professorUpdate.setGender(professor.getGender());
            professorUpdate.setAddress(professor.getAddress());
            professorUpdate.setPhone(professor.getPhone());

            Users userUpdate = usersRepository.findByDni(professorUpdate.getDni());
            if (userUpdate != null) {
                userUpdate.setUsername(user.getUsername());
                userUpdate.setFull_name(user.getFull_name());
                userUpdate.setDni(user.getDni());
                usersRepository.save(userUpdate);
            }

            professorRepository.save(professorUpdate);

            data.put("message", "Informacion actualizada correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            data.put("error", "No se pudo actualizar la informacion. El nombre de usuario ya existe en la base de datos.");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            data.put("error", "No se pudo actualizar la informacion " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar su password
    public ResponseEntity<Object> updatePassword(int id, String newPassword) {
        data = new HashMap<>();

        try {
            // Se busca al profesor por ID
            Professor professor = professorRepository.findById(id).orElse(null);

            // Se verifica si existe
            if (professor != null) {
                // Se busca su usuario por DNI
                Users user = usersRepository.findByDni(professor.getDni());

                // Se actualiza y se encripta la nueva contrasena
                user.setPassword(hashPassword.encriptarPassword(newPassword));
                // Se guarda
                usersRepository.save(user);
                data.put("message", "Contrasena actualizada correctamente");
                return new ResponseEntity<>(data, HttpStatus.OK);

            } else {
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
            }


        } catch (Exception ex) {
            data.put("error", "Error al actualizar la contrasean: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
