package com.ead.authuser.controllers;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.UserCourseDto;
import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserCourseService;
import com.ead.authuser.services.UserService;
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
import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserCourseController {

    @Autowired
    private CourseClient courseClient;

    @Autowired
    private UserService userService;

    @Autowired
    private UserCourseService userCourseService;

    @GetMapping("/users/{userId}/courses")
    public ResponseEntity<Page<CourseDto>> getAllCoursesByUser(@PageableDefault(sort = "courseId", direction = Sort.Direction.ASC) Pageable page,
                                                               @PathVariable("userId") UUID userId) {
        log.debug("GET getAllCoursesByUser");
        return ResponseEntity.status(HttpStatus.OK).body(courseClient.getAllCoursesByUser(userId, page));
    }

    @PostMapping("/users/{userId}/courses/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable("userId") UUID userId, @RequestBody @Valid UserCourseDto userCourseDto) {
        log.debug("POST saveSubscriptionUserInCourse");

        Optional<UserModel> userModelOptional = userService.findById(userId);
        if (!userModelOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        // Verifica se o usuário já está inscrito antes de tentar salvar
        if (userCourseService.existsByUserAndCourseId(userId, userCourseDto.getCourseId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already subscribed to this course");
        }

        UserCourseModel userCourseModel = new UserCourseModel(null, userModelOptional.get(), userCourseDto.getCourseId());
        userCourseService.save(userCourseModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(userCourseModel);
    }

    @DeleteMapping("/users/courses/{courseId}")
    public ResponseEntity<Object> deleteUserCourseByCourse(@PathVariable("courseId") UUID courseId) {
        log.debug("DELETE deleteUserCourseByCourse");

        if(!userCourseService.existsByCourseId(courseId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("UserCourse not found");
        }

        userCourseService.deleteUserCourseByCourse(courseId);

        return ResponseEntity.status(HttpStatus.OK).body("User course deleted");
    }

}
