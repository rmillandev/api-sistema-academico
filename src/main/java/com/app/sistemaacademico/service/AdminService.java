package com.app.sistemaacademico.service;

import com.app.sistemaacademico.model.*;
import com.app.sistemaacademico.repository.*;
import com.app.sistemaacademico.util.dto.CourseResponseDTO;
import com.app.sistemaacademico.util.HashPassword;
import com.app.sistemaacademico.util.dto.InscriptionResponseDTO;
import com.app.sistemaacademico.util.dto.StudentCoursesDTO;
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
public class AdminService {

    @Autowired
    private IAdminRepository adminRepository;

    @Autowired
    private IStudentRepository studentRepository;

    @Autowired
    private IProfessorRepository professorRepository;

    @Autowired
    private ICourseRepository courseRepository;

    @Autowired
    private IInscriptionRepository inscriptionRepository;

    @Autowired
    private IUsersRepository usersRepository;

    @Autowired
    private HashPassword hashPassword;

    HashMap<String, Object> data;


    // Obtener todos los estudiantes
    public Page<Student> getAllStudents(Pageable pageable) {
        return studentRepository.findAll(pageable);
    }

    // Obtener un estudiante por ID
    public ResponseEntity<Object> getStudentById(int id){
        data = new HashMap<>();

        Optional<Student> result = studentRepository.findById(id);

        if(result.isPresent()){
            data.put("Student", result);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }else{
            data.put("message", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }

    }

    // Guardar un estudiante
    public ResponseEntity<Object> createStudent(Users user, Student student){
        data = new HashMap<>();

        try{
            // Se encripta la password
            String encryptedPassword = hashPassword.encriptarPassword(user.getPassword());

            // Se le asigna la password encriptada
            user.setPassword(encryptedPassword);

            // Se guarda el usuario
            Users savedUser = usersRepository.save(user);

            // Se guarda el estudiante
            Student savedStudent = studentRepository.save(student);

            // Respuesta
            data.put("message", "Registro creado correctamente");
            data.put("student-body", savedStudent);
            data.put("student-credential", savedUser);

            return new ResponseEntity<>(data, HttpStatus.CREATED);
        }
        catch (Exception ex){
            data.put("error", "No se pudo guardar el registro " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateStudent(int id, Student student, Users user){
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
                data.put("error", "Estudiante no encontrado");
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
                // el admin no podra actualizar la contrasena, solo podra el mismo usuario que le corresponda
            }

            studentRepository.save(studentUpdate);

            data.put("message", "Estudiante actualizado correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex){
            data.put("error", "No se pudo actualizar el registro. El nombre de usuario ya existe en la base de datos.");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            data.put("error", "No se pudo actualizar el registro " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un estudiante y su usuario
    public ResponseEntity<Object> deleteStudent(int id){
        data = new HashMap<>();

        // Verificar si el ID es menor o igual a 0, si es asi lanza un error
        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        // Obtener el estudiante por el ID
        Optional<Student> result = studentRepository.findById(id);

        // Se verifica si existe el estudiante con ese ID
        if(!result.isPresent()){
            data.put("error", "Estudiante no encontrado");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Student student = result.get();

            // Obtener el DNI del estudiante para asi buscar sus credenciales
            long dni = student.getDni();

            // Buscar y eliminar al usuario por DNI
            Users user = usersRepository.findByDni(dni);
            if(user != null){
                usersRepository.delete(user);
            }

            // eliminar estudiante
            studentRepository.delete(student);

            data.put("message", "Estudiante eliminado correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "Error al eliminar el estudiante: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // *********************************************************


    // Obtener todos los profesores
    public Page<Professor> getAllProfessors(Pageable pageable){
        return professorRepository.findAll(pageable);
    }

    // Obtener un profesor por ID
    public ResponseEntity<Object> getProfessorById(int id){
        data = new HashMap<>();

        Optional<Professor> result = professorRepository.findById(id);

        if(result.isPresent()){
            data.put("Professor", result);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }else{
            data.put("message", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
    }

    // Crear profesor
    public ResponseEntity<Object> createProfessor(Users user, Professor professor){
        data = new HashMap<>();

        try{
            // Se encripta la contrasena
            String encryptedPassword = hashPassword.encriptarPassword(user.getPassword());

            // Se le asigna la password encriptada
            user.setPassword(encryptedPassword);

            // Se guarda el usuario
            Users savedUser = usersRepository.save(user);

            // Se guarda el profesor
            Professor savedProfessor = professorRepository.save(professor);

            // Respuesta
            data.put("message", "Registro creado correctamente");
            data.put("professor-body", savedProfessor);
            data.put("professor-credential", savedUser);

            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (DataIntegrityViolationException ex){
            data.put("error", "No se pudo guardar el registro. El nombre de usuario ya existe en la base de datos.");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            data.put("error", "No se pudo guardar el registro " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar profesor
    public ResponseEntity<Object> updateProfessor(int id, Professor professor, Users user){
        data = new HashMap<>();

        try{
            // Verificar si el ID es menor o igual a 0, si es asi lanza un error
            if(id <= 0){
                data.put("error", "ID no existe");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }

            // Se busca al profesor por el ID
            Optional<Professor> result = professorRepository.findById(id);

            // Se verifica si existe
            if(!result.isPresent()){
                data.put("error", "Profesor no encontrado");
                return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
            }

            Professor professorUpdate = result.get();
            professorUpdate.setFull_name(professor.getFull_name());
            professorUpdate.setDni(professor.getDni());
            professorUpdate.setGender(professor.getGender());
            professorUpdate.setAddress(professor.getAddress());
            professorUpdate.setPhone(professor.getPhone());

            Users userUpdate = usersRepository.findByDni(professorUpdate.getDni());
            if(userUpdate != null){
                userUpdate.setUsername(user.getUsername());
                userUpdate.setFull_name(user.getFull_name());
                userUpdate.setDni(user.getDni());
                usersRepository.save(userUpdate);
                // el admin no podra actualizar la contrasena, solo podra el mismo usuario que le corresponda
            }

            professorRepository.save(professorUpdate);

            data.put("message", "Profesor actualizado correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (DataIntegrityViolationException ex){
            data.put("error", "No se pudo actualizar el registro. El nombre de usuario ya existe en la base de datos.");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } catch (Exception ex){
            data.put("error", "No se pudo actualizar el registro " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar profesor
    public ResponseEntity<Object> deleteProfessor(int id){
        data = new HashMap<>();

        // Verificar si el ID es menor o igual a 0, si es asi lanza un error
        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        // Obtener el profesor por el ID
        Optional<Professor> result = professorRepository.findById(id);

        // Se verifica si existe el profesor con ese ID
        if(!result.isPresent()){
            data.put("error", "Profesor no encontrado");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Professor professor = result.get();

            Users user = usersRepository.findByDni(professor.getDni());
            if(user != null){
                usersRepository.delete(user);
            }

            // Eliminar profesor
            professorRepository.delete(professor);

            data.put("message", "Profesor eliminado correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "Error al eliminar al profesor: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // *********************************************************


    // Obtener todos los cursos
    public Page<Course> getAllCourses(Pageable pageable){
        return courseRepository.findAll(pageable);
    }

    // Obtener un curso por ID
    public ResponseEntity<Object> getCourseById(int id){
        data = new HashMap<>();

        Optional<Course> result = courseRepository.findById(id);

        if(result.isPresent()){
            data.put("Course", result);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }else{
            data.put("message", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.NOT_FOUND);
        }
    }

    // Crear un curso
    public ResponseEntity<Object> createCourse(Course course){
        data = new HashMap<>();

        try{
            // Se guarda el curso
            Course savedCourse = courseRepository.save(course);

            // Se crea una nueva instancia de course DTO y se asignan los datos
            CourseResponseDTO courseResponseDTO = new CourseResponseDTO();
            courseResponseDTO.setId(savedCourse.getId());
            courseResponseDTO.setName(savedCourse.getName());
            courseResponseDTO.setDescription(savedCourse.getDescription());
            courseResponseDTO.setDuration(savedCourse.getDuration());

            // Se busca al profesor por ID
            Professor professorId = savedCourse.getProfessor();
            Optional<Professor> professor = professorRepository.findById(professorId.getId());

            // Se verifica si existe y se  asigna al professor a la DTO
            // Usar :: simplifica el metodo, si existe un valor este se agregar a la DTO
            professor.ifPresent(courseResponseDTO::setProfessor);

            data.put("message", "Registro creado exitosamente");
            data.put("body", courseResponseDTO);
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "No se pudo guardar el registro " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar un curso
    public ResponseEntity<Object> updateCourse(int id, Course course){
        data = new HashMap<>();

        // Verificar si el ID es menor o igual a 0, si es asi lanza un error
        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        // Obtener el courso por el ID
        Optional<Course> result = courseRepository.findById(id);

        // Se verifica si existe el curso con ese ID
        if(!result.isPresent()){
            data.put("error", "Curso no encontrado");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Course courseUpdate = result.get();
            courseUpdate.setName(course.getName());
            courseUpdate.setDescription(course.getDescription());
            courseUpdate.setDuration(course.getDuration());
            courseUpdate.setProfessor(course.getProfessor());

            courseRepository.save(courseUpdate);

            data.put("message", "Curso actualizado correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "Error al actualizar el curso: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un curso
    public ResponseEntity<Object> deleteCourse(int id){
        data = new HashMap<>();

        // Verificar si el ID es menor o igual a 0, si es asi lanza un error
        if(id <= 0){
            data.put("error", "ID no existe");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        // Obtener el courso por el ID
        Optional<Course> result = courseRepository.findById(id);

        // Se verifica si existe el curso con ese ID
        if(!result.isPresent()){
            data.put("error", "Curso no encontrado");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        try{
            Course course = result.get();

            // Eliminar curso
            courseRepository.delete(course);

            data.put("message", "Curso eliminado correctamente");
            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "Error al eliminar el curso: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    // *********************************************************


    // Inscribir estudiante a los cursos
    public ResponseEntity<Object> enrollStudent(Inscription inscription){
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

            data.put("message", "Estudiante inscrito correctamente");
            data.put("body", inscriptionResponseDTO);

            return new ResponseEntity<>(data, HttpStatus.OK);
        }catch (Exception ex){
            data.put("error", "No se pudo inscribir al estudiante al curso: " + ex.getMessage());
            return new ResponseEntity<>(data, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<StudentCoursesDTO> getStudentCourses(long dni) throws Exception {
        try{
            return courseRepository.getStudentCourses(dni).stream()
                    .map(result -> {
                        StudentCoursesDTO studentCoursesDTO = new StudentCoursesDTO();
                        studentCoursesDTO.setName((String) result[0]);
                        studentCoursesDTO.setDescription((String) result[1]);
                        studentCoursesDTO.setDuration((String) result[2]);
                        studentCoursesDTO.setProfessor((String) result[3]);
                        return studentCoursesDTO;
                    }).collect(Collectors.toList());
        }catch (Exception ex){
            throw new Exception("Error al obtener los cursos del estudiante: " + ex.getMessage());
        }
    }

}
