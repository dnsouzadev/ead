package com.ead.authuser.controllers;

import com.ead.authuser.dtos.UserDto;
import com.ead.authuser.enums.UserStatus;
import com.ead.authuser.enums.UserType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.services.UserService;
import com.ead.authuser.specifications.SpecificationTemplate;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Log4j2
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllUsers(
            @RequestParam(required = false) UserType userType,
            @RequestParam(required = false) UserStatus userStatus,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String fullName,
            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable page,
            @RequestParam(required = false) UUID courseId) {

        log.debug("GET getAllUsers");
        Specification<UserModel> spec = Specification.where(null);

        if (userType != null) {
            spec = spec.and(SpecificationTemplate.userTypeEquals(userType));
        }
        if (userStatus != null) {
            spec = spec.and(SpecificationTemplate.userStatusEquals(userStatus));
        }
        if (email != null) {
            spec = spec.and(SpecificationTemplate.emailLike(email));
        }
        if (fullName != null) {
            spec = spec.and(SpecificationTemplate.fullNameLike(fullName));
        }

        Page<UserModel> userModelPage = null;

        if (courseId != null) {
            userModelPage = userService.findAll(SpecificationTemplate.userCourseId(courseId).and(spec), page);
        } else {
            userModelPage = userService.findAll(spec, page);
        }

        if (!userModelPage.isEmpty()) {
            for(UserModel user : userModelPage.toList()) {
                user.add(linkTo(methodOn(UserController.class).getOneUser(user.getId())).withSelfRel());
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(userModelPage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getOneUser(@PathVariable(value = "id") UUID id) {
        log.debug("GET getOneUser id received {}", id);
        Optional<UserModel> userModelOptional = userService.findById(id);
        return userModelOptional.<ResponseEntity<Object>>map(userModel -> ResponseEntity.status(HttpStatus.OK).body(userModel)).orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable(value = "id") UUID id) {
        log.debug("DELETE deleteUser id received {}", id);
        Optional<UserModel> userModelOptional = userService.findById(id);
        if (userModelOptional.isPresent()) {
            userService.delete(userModelOptional.get());
            log.info("User deleted successfully userId {}", id);
            return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateUser(@PathVariable(value = "id") UUID id, @RequestBody
                                            @Validated(UserDto.UserView.UserPut.class)
                                            @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        log.debug("PUT updateUser userDto received {}", userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(id);
        if (userModelOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        var userModel = userModelOptional.get();
        userModel.setFullName(userDto.getFullName());
        userModel.setPhoneNumber(userDto.getPhoneNumber());
        userModel.setCpf(userDto.getCpf());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("PUT updateUser userModel userId {}", userModel.getId());
        log.info("User updated successfully userId {}", userModel.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<Object> updatePassword(@PathVariable(value = "id") UUID id, @RequestBody
                                                @Validated(UserDto.UserView.PasswordPut.class)
                                                @JsonView(UserDto.UserView.PasswordPut.class) UserDto userDto) {
        log.debug("PUT updatePassword userDto received {}", userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(id);
        if (userModelOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        var userModel = userModelOptional.get();
        if (!userModel.getPassword().equals(userDto.getOldPassword())) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Old password is incorrect");
        userModel.setPassword(userDto.getPassword());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("PUT updatePassword userModel userId {}", userModel.getId());
        log.info("Password updated successfully userId {}", userModel.getId());
        return ResponseEntity.status(HttpStatus.OK).body("Password updated successfully");
    }

    @PutMapping("/{id}/image")
    public ResponseEntity<Object> updateImage(@PathVariable(value = "id") UUID id, @RequestBody
                                            @Validated(UserDto.UserView.ImagePut.class)
                                            @JsonView(UserDto.UserView.ImagePut.class) UserDto userDto) {
        log.debug("PUT updateImage userDto received {}", userDto.toString());
        Optional<UserModel> userModelOptional = userService.findById(id);
        if (userModelOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        var userModel = userModelOptional.get();
        userModel.setImageUrl(userDto.getImageUrl());
        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userService.save(userModel);
        log.debug("PUT updateImage userModel userId {}", userModel.getId());
        log.info("Image updated successfully userId {}", userModel.getId());
        return ResponseEntity.status(HttpStatus.OK).body(userModel);
    }
}
