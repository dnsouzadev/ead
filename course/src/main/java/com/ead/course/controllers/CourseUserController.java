package com.ead.course.controllers;
import com.ead.course.dtos.SubscriptionDto;
import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.services.CourseService;
import com.ead.course.services.UserService;
import com.ead.course.specifications.SpecificationTemplate;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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
    private CourseService courseService;

    @Autowired
    private UserService userService;

    @GetMapping("/courses/{courseId}/users")
    public ResponseEntity<Object> getAllUsersByCourse(@RequestParam(required = false) String email,
                                                      @RequestParam(required = false) String fullName,
                                                      @RequestParam(required = false) UserType userType,
                                                      @RequestParam(required = false) UserStatus userStatus,
                                                      @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
                                                             @PathVariable("courseId") UUID courseId) {
        log.debug("GET getAllUsersByCourse");
        Specification<UserModel> spec = Specification.where(null);

        if(email != null) spec = spec.and(SpecificationTemplate.emailLikeUser(email));
        if(fullName != null) spec = spec.and(SpecificationTemplate.fullNameLikeUser(fullName));
        if(userType != null) spec = spec.and(SpecificationTemplate.UserTypeUserEquals(userType));
        if(userStatus != null) spec = spec.and(SpecificationTemplate.UserStatusUserEquals(userStatus));

        Optional<CourseModel> courseModelOptional = courseService.findById(courseId);
        if (!courseModelOptional.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), page));
    }

    @PostMapping("/courses/{courseId}/users/subscription")
    public ResponseEntity<Object> saveSubscriptionUserInCourse(@PathVariable("courseId") UUID courseId, @RequestBody @Valid SubscriptionDto subscriptionDto) {
        log.debug("POST saveSubscriptionUserInCourse");

        Optional<CourseModel> courseExists = courseService.findById(courseId);
        if (!courseExists.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Course not found");

        return ResponseEntity.status(HttpStatus.CREATED).body("");
    }
}
