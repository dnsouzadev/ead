package com.ead.course.specifications;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
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
            Root<ModuleModel> module = root;
            Root<CourseModel> course = query.from(CourseModel.class);
            Expression<Collection<ModuleModel>> coursesModules = course.get("modules");
            return criteriaBuilder.and(criteriaBuilder.equal(course.get("courseId"), courseId),
                    criteriaBuilder.isMember(module, coursesModules));
        };
    }
}
