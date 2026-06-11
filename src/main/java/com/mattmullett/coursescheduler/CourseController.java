package com.mattmullett.coursescheduler;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class CourseController {

    private List<Course> courses = new ArrayList<>();

    public CourseController() {
        courses.add(new Course(1, "CSCI 2110", "Data Structures", "Various"));
        courses.add(new Course(2, "CSCI 2134", "Software Development", "Various"));
        courses.add(new Course(3, "CSCI 3130", "Software Engineering", "Bodorik"));
    }

    // GET all courses
    @GetMapping("/courses")
    public List<Course> getAllCourses() {
        return courses;
    }

    // GET one course by id
    @GetMapping("/courses/{id}")
    public Course getCourseById(@PathVariable int id) {
        for (Course course : courses) {
            if (course.getId() == id) {
                return course;
            }
        }
        return null; // we'll improve this later
    }

    // CREATE a new course
    @PostMapping("/courses")
    public Course addCourse(@RequestBody Course newCourse) {
        courses.add(newCourse);
        return newCourse;
    }

    // UPDATE an existing course
    @PutMapping("/courses/{id}")
    public Course updateCourse(@PathVariable int id, @RequestBody Course updatedCourse) {
        for (Course course : courses) {
            if (course.getId() == id) {
                course.setCode(updatedCourse.getCode());
                course.setName(updatedCourse.getName());
                course.setInstructor(updatedCourse.getInstructor());
                return course;
            }
        }
        return null;
    }

    // DELETE a course
    @DeleteMapping("/courses/{id}")
    public String deleteCourse(@PathVariable int id) {
        courses.removeIf(course -> course.getId() == id);
        return "Deleted course with id " + id;
    }
}