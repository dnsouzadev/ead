package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UtilsServiceImpl implements UtilsService {
    String REQUEST_URI = "http://localhost:8087";

    public String createUrl(String courseId, Pageable pageable) {
        return REQUEST_URI + "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=id".replaceAll(":", ",");
    }
}
