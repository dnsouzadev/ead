package com.ead.course.controllers;

import com.ead.course.clients.CourseClient;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.CourseUserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseUserController {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseUserService courseUserService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Page<UserDto>> getAllUsersByCourse(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
                                                             @PathVariable("courseId") UUID userId) {
        log.debug("GET getAllUsersByCourse");
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, page));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable("courseId") UUID courseId, @RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("POST saveSubscriptionUserInCourse");

        Optional<CourseModel> courseExists = courseService.findById(courseId);
        if (!courseExists.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

        if(courseUserService.existsByCourseAndUserId(courseExists.get(), subscriptionDto.getUserId())) return ResponseEntity.status(HttpStatus.CONFLICT).body("User already subscribed to this course");

        CourseUserModel courseUserModel = courseUserService.save(courseExists.get().convertToCourseUserModel(subscriptionDto.getUserId()));


        return ResponseEntity.status(HttpStatus.CREATED).body(courseUserModel);
    }
}
