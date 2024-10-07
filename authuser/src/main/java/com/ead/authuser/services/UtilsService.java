package com.ead.authuser.services;

import org.springframework.data.domain.Pageable;

public interface UtilsService {

    String createUrlGetAllCoursesByUserId(String userId, Pageable pageable);
}
