package com.mattmullett.coursescheduler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class StudentController {

    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    // Spring injects both repositories
    public StudentController(StudentRepository studentRepository, CourseRepository courseRepository) {
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    // GET all students
    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // GET one student by id
    @GetMapping("/students/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable int id) {
        return studentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE a student
    @PostMapping("/students")
    public Student addStudent(@RequestBody Student newStudent) {
        return studentRepository.save(newStudent);
    }

    // DELETE a student
    @DeleteMapping("/students/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable int id) {
        if (studentRepository.existsById(id)) {
            studentRepository.deleteById(id);
            return ResponseEntity.ok("Deleted student with id " + id);
        }
        return ResponseEntity.notFound().build();
    }

    // ENROLL a student in a course — the interesting one
    @PostMapping("/students/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> enroll(@PathVariable int studentId, @PathVariable int courseId) {
        // Find the student
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        // Find the course
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            return ResponseEntity.notFound().build();
        }
        // Link them and save
        student.enroll(course);
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    // UNENROLL a student from a course
    @DeleteMapping("/students/{studentId}/enroll/{courseId}")
    public ResponseEntity<Student> unenroll(@PathVariable int studentId, @PathVariable int courseId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        // Remove the course from the student's set by matching id
        student.getCourses().removeIf(course -> course.getId() == courseId);
        studentRepository.save(student);
        return ResponseEntity.ok(student);
    }
}