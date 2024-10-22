package com.ead.course.specifications;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.enums.UserStatus;
import com.ead.course.enums.UserType;
import com.ead.course.models.CourseModel;
import com.ead.course.models.UserModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.UUID;

public class SpecificationTemplate {

    public static Specification<CourseModel> courseLevelEquals(CourseLevel courseLevel) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("courseLevel"), courseLevel);
    }

    public static Specification<CourseModel> courseStatusEquals(CourseStatus courseStatus) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("courseStatus"), courseStatus);
    }

    public static Specification<CourseModel> nameLike(String name) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<UserModel> emailLikeUser(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<UserModel> fullNameLikeUser(String fullName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

    public static Specification<UserModel> UserStatusUserEquals(UserStatus userStatus) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userStatus"), userStatus);
    }

    public static Specification<UserModel> UserTypeUserEquals(UserType userType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.notEqual(root.get("userType"), userType);
    }

    public static Specification<ModuleModel> titleLikeModule(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<LessonModel> titleLikeLesson(String title) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("title"), "%" + title + "%");
    }

    public static Specification<ModuleModel> moduleCourseId(final UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<ModuleModel, CourseModel> courseJoin = root.join("course");
            return criteriaBuilder.equal(courseJoin.get("id"), courseId);
        };
    }

    public static Specification<LessonModel> lessonModuleId(UUID moduleId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Join<LessonModel, ModuleModel> moduleJoin = root.join("module");
            return criteriaBuilder.equal(moduleJoin.get("id"), moduleId);
        };
    }

    public static Specification<UserModel> userCourseId(final UUID courseId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<UserModel> user = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<UserModel>> coursesUsers = course.get("users");
            return criteriaBuilder.and(criteriaBuilder.equal(course.get("id"), courseId), criteriaBuilder.isMember(user, coursesUsers));
        };
    }

    public static Specification<CourseModel> courseUserId(final UUID userId) {
        return (root, query, criteriaBuilder) -> {
            query.distinct(true);
            Root<CourseModel> course = root;
            Root<UserModel> user = query.from(UserModel.class);
            Expression<Collection<CourseModel>> usersCourses = user.get("courses");
            return criteriaBuilder.and(criteriaBuilder.equal(user.get("id"), userId), criteriaBuilder.isMember(course, usersCourses));
        };
    }
}
