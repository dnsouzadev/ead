package com.ead.authuser.specifications;

import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;

public class SpecificationTemplate {

    public static Specification<UserModel> userTypeEquals(UserType userType) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userType"), userType);
    }

    public static Specification<UserModel> userStatusEquals(UserStatus userStatus) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("userStatus"), userStatus);
    }

    public static Specification<UserModel> emailLike(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<UserModel> fullNameLike(String fullName) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get("fullName"), "%" + fullName + "%");
    }

}

