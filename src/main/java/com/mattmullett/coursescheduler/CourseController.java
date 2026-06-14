package com.mattmullett.coursescheduler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final StudentRepository studentRepository;

    // Spring injects both repositories automatically
    public CourseController(CourseRepository courseRepository, StudentRepository studentRepository) {
        this.courseRepository = courseRepository;
        this.studentRepository = studentRepository;
    }

    // GET all courses
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    // GET one course by id
    @GetMapping("/courses/{id}")
    public ResponseEntity<Course> getCourseById(@PathVariable int id) {
        return courseRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // CREATE a new course
    @PostMapping("/courses")
    public Course addCourse(@RequestBody Course newCourse) {
        return courseRepository.save(newCourse);
    }

    // UPDATE an existing course
    @PutMapping("/courses/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        return courseRepository.findById(id)
                .map(course -> {
                    course.setCode(updatedCourse.getCode());
                    course.setName(updatedCourse.getName());
                    course.setInstructor(updatedCourse.getInstructor());
                    return ResponseEntity.ok(courseRepository.save(course));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE a course
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable int id) {
        if (courseRepository.existsById(id)) {
            courseRepository.deleteById(id);
            return ResponseEntity.ok("Deleted course with id " + id);
        }
        return ResponseEntity.notFound().build();
    }

    // GET the roster — students enrolled in a course
    @GetMapping("/courses/{id}/students")
    public ResponseEntity<List<Student>> getRoster(@PathVariable int id) {
        if (!courseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        List<Student> roster = studentRepository.findAll().stream()
                .filter(student -> student.getCourses().stream()
                        .anyMatch(course -> course.getId() == id))
                .toList();
        return ResponseEntity.ok(roster);
    }
}