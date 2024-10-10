package com.ead.authuser.repositories;

import com.ead.authuser.models.UserCourseModel;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface UserCourseRepository extends JpaRepository<UserCourseModel, UUID> {

    @Query(value = "SELECT * FROM tb_user_course WHERE user_id = :userId", nativeQuery = true)
    List<UserCourseModel> findAllUserCourseIntoUser(@Param("userId") UUID userId);

    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM tb_users_courses WHERE user_id = :userId AND course_id = :courseId", nativeQuery = true)
    boolean existsByUserAndCourseId(@Param("userId") UUID userId, @Param("courseId") UUID courseId);


    boolean existsByCourseId(UUID courseId);

    void deleteAllByCourseId(UUID courseId);
}
