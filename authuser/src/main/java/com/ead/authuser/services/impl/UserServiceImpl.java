package com.ead.authuser.services.impl;

import com.ead.authuser.clients.CourseClient;
import com.ead.authuser.dtos.UserEventDto;
import com.ead.authuser.enums.ActionType;
import com.ead.authuser.models.UserModel;
import com.ead.authuser.publishers.UserEventPublisher;
import com.ead.authuser.repositories.UserRepository;
import com.ead.authuser.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CourseClient courseClient;

    @Autowired
    UserEventPublisher userEventPublisher;

    @Override
    public List<UserModel> findAll() {
        return userRepository.findAll();
    }

    @Override
    public Optional<UserModel> findById(UUID id) {
        return userRepository.findById(id);
    }

    @Transactional
    @Override
    public void delete(UserModel userModel) {
        userRepository.delete(userModel);
    }

    @Override
    public UserModel save(UserModel userModel) {
        return userRepository.save(userModel);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public Page<UserModel> findAll(Specification<UserModel> spec, Pageable page) {
        return userRepository.findAll(spec, page);
    }

    @Override
    @Transactional
    public UserModel saveUser(UserModel userModel) {
        UserModel user = save(userModel);
        userEventPublisher.publisherUserEvent(user.convertToUserEventDto(), ActionType.CREATE);
        return user;
    }

    @Transactional
    @Override
    public void deleteUser(UserModel userModel) {
        userRepository.delete(userModel);
        userEventPublisher.publisherUserEvent(userModel.convertToUserEventDto(), ActionType.DELETE);
    }

    @Transactional
    @Override
    public UserModel updateUser(UserModel userModel) {
        UserModel user = save(userModel);
        userEventPublisher.publisherUserEvent(user.convertToUserEventDto(), ActionType.UPDATE);
        return user;
    }

    @Override
    public UserModel updatePassword(UserModel userModel) {
        return save(userModel);
    }
}
