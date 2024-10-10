package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface UserCourseService {
    boolean existsByUserAndCourseId(UUID userId, UUID courseId);

    UserCourseModel save(UserCourseModel userCourseModel);

    boolean existsByCourseId(UUID courseId);

    void deleteUserCourseByCourse(UUID courseId);
}
