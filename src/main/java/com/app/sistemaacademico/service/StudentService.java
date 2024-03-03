package com.app.sistemaacademico.service;

import com.app.sistemaacademico.model.Course;
import com.app.sistemaacademico.model.Inscription;
import com.app.sistemaacademico.model.Student;
import com.app.sistemaacademico.model.Users;
import com.app.sistemaacademico.repository.ICourseRepository;
import com.app.sistemaacademico.repository.IInscriptionRepository;
import com.app.sistemaacademico.repository.IStudentRepository;
import com.app.sistemaacademico.repository.IUsersRepository;
import com.app.sistemaacademico.util.HashPassword;
import com.app.sistemaacademico.util.dto.CoursesFromStudentDTO;
import com.app.sistemaacademico.util.dto.InscriptionResponseDTO;
import com.app.sistemaacademico.util.dto.ProfessorStudentCredentialDTO;
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
public class StudentService {

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IInscriptionRepository inscriptionRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private HashPassword hashPassword;

    HashMap<String, Object> data;


    // Obtener informacion del estudiante
    public List<ProfessorStudentCredentialDTO> getInformationStudent(long dni) throws Exception {
        try{
            return studentRepository.getInformationStudent(dni).stream()
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
        }catch (Exception ex){
            throw new Exception("Error al obtener la informacion del profesor: " + ex.getMessage());
        }
    }

    // Obtener los cursos a los que esta inscrito
    public Page<CoursesFromStudentDTO> getCoursesFromStudent(Pageable pageable, long dni) throws Exception {
        try{
            // Consulta pagina
            Page<Object[]> resultPage = studentRepository.getCoursesFromStudent(dni, pageable);

            // Se mapea el resultado DTO
            return resultPage.map(result -> {
               CoursesFromStudentDTO coursesFromStudentDTO = new CoursesFromStudentDTO();
               coursesFromStudentDTO.setName((String) result[0]);
               coursesFromStudentDTO.setDescription((String) result[1]);
               coursesFromStudentDTO.setDuration((String) result[2]);
               coursesFromStudentDTO.setProfessor((String) result[3]);
               return coursesFromStudentDTO;
            });
        }catch (Exception ex){
            throw new Exception("Error al obtener la informacion del profesor: " + ex.getMessage());
        }
    }

    // Inscribirse a un curso
    public ResponseEntity<Object> inscription(Inscription inscription){
        data = new HashMap<>();

        try{
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

            data.put("message", "Se inscribio correctamente al curso");
            data.put("body", inscriptionResponseDTO);

            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "No se pudo inscribir al curso: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar su informacion
    public ResponseEntity<Object> updateInformation(int id, Users user, Student student){
        data = new HashMap<>();

        try{
            // Verificar si el ID es menor o igual a 0, si es asi lanza un error
            if(id <= 0){
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }

            // Obtener el estudiante por el ID
            Optional<Student> result = studentRepository.findById(id);

            // Se verifica si existe el estudiante con ese ID
            if(!result.isPresent()){
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }

            Student studentUpdate = result.get();
            studentUpdate.setFull_name(student.getFull_name());
            studentUpdate.setDni(student.getDni());
            studentUpdate.setGender(student.getGender());
            studentUpdate.setAddress(student.getAddress());
            studentUpdate.setPhone(student.getPhone());

            Users userUpdate = usersRepository.findByDni(studentUpdate.getDni());
            if(userUpdate != null){
                userUpdate.setUsername(user.getUsername());
                userUpdate.setFull_name(user.getFull_name());
                userUpdate.setDni(user.getDni());
                usersRepository.save(userUpdate);
            }

            studentRepository.save(studentUpdate);

            data.put("message", "Informacion actualizada correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (DataIntegrityViolationException ex){
            data.put("error", "No se pudo actualizar la informacion. El nombre de usuario ya existe en la base de datos.");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            data.put("error", "No se pudo actualizar la informacion " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar su password
    public ResponseEntity<Object> updatePassword(int id, String newPassword){
        data = new HashMap<>();

        try{
            // Se busca el estudiante por ID
            Student student = studentRepository.findById(id).orElse(null);

            // Se verifica si existe
            if(student != null){
                // Se busca el usuario por su DNI
                Users user = usersRepository.findByDni(student.getDni());

                // Se actualiza y se encripta la nueva contrasena
                user.setPassword(hashPassword.encriptarPassword(newPassword));
                // Se guarda
                usersRepository.save(user);
                data.put("message", "Contrasena actualizada correctamente");
                return new ResponseEntity<>(data, HttpStatus.OK);

            }else {
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
            }

        }catch (Exception ex){
            data.put("error", "Error al actualizar la contrasena: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
