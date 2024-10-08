package com.ead.authuser.services;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public interface UserCourseService {
    boolean existsByCourseAndUserId(UserModel userModel, UUID userId);

    UserCourseModel save(UserCourseModel userCourseModel);
}
