package com.ead.course.specifications;

import com.ead.course.enums.CourseLevel;
import com.ead.course.enums.CourseStatus;
import com.ead.course.models.CourseModel;
import org.springframework.data.jpa.domain.Specification;

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
}
