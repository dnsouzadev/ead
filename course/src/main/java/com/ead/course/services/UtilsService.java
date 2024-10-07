package com.ead.course.services;

import org.springframework.data.domain.Pageable;

public interface UtilsService {
    String createUrl(String courseId, Pageable pageable);
}
