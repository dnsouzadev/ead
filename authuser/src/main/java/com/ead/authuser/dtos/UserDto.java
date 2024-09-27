package com.ead.authuser.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import java.util.UUID;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    public interface UserView {
        public static interface RegistrationPost {}
        public static interface UserPut {}
        public static interface PasswordPut {}
        public static interface ImagePut {}
    }

    private UUID userId;

    @JsonView(UserView.RegistrationPost.class)
    private String username;

    @JsonView(UserView.RegistrationPost.class)
    private String email;

    @JsonView({UserView.RegistrationPost.class, UserView.PasswordPut.class})
    private String password;

    @JsonView(UserView.PasswordPut.class)
    private String oldPassword;

    @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class})
    private String fullName;

    @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class})
    private String phoneNumber;

    @JsonView({UserView.UserPut.class, UserView.RegistrationPost.class})
    private String cpf;

    @JsonView(UserView.ImagePut.class)
    private String imageUrl;

}
