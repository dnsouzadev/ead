package com.ead.authuser.services;

import com.ead.authuser.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserModel> findAll();

    Optional<UserModel> findById(UUID id);

    void delete(UUID id);

    void save(UserModel userModel);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<UserModel> findAll(Pageable page);
}
