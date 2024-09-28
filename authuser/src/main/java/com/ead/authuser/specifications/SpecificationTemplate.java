package com.ead.authuser.specifications;

import com.ead.authuser.models.UserModel;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.Like;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

public class SpecificationTemplate {

    public static Specification<UserModel> userTypeEquals(String userType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userType"), userType);
    }

    public static Specification<UserModel> userStatusEquals(String userStatus) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userStatus"), userStatus);
    }

    public static Specification<UserModel> emailLike(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }
}
