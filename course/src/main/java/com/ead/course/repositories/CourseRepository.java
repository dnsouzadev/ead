package com.ead.course.repositories;

import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface CourseRepository extends JpaRepository<CourseModel, UUID>, JpaSpecificationExecutor<CourseModel> {

    @Query(value="SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM tb_courses_users c WHERE c.course_id = :courseId AND c.user_id = :userId", nativeQuery = true)
    boolean existsByCourseAndUser(UUID courseId, UUID userId);

    @Modifying
    @Query(value = "INSERT INTO tb_courses_users (course_id, user_id) VALUES (:courseId, :userId)", nativeQuery = true)
    void saveSubscriptionUserInCourse(UUID courseId, UUID userId);

    @Modifying
    @Query(value = "DELETE FROM tb_courses_users WHERE course_id = :courseId", nativeQuery = true)
    void deleteCourseUserByCourse(@Param("courseId") UUID courseId);

    @Modifying
    @Query(value = "DELETE FROM tb_courses_users WHERE user_id = :userId", nativeQuery = true)
    void deleteCourseUserByUser(@Param("userId") UUID userId);
}
