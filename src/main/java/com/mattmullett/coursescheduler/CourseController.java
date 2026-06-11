package com.mattmullett.coursescheduler;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {

    @GetMapping("/hello")
    public String hello() {
        return "Course scheduler is alive";
    }
}