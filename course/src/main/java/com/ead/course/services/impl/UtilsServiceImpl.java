package com.ead.course.services.impl;

import com.ead.course.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UtilsServiceImpl implements UtilsService {

    public String createUrlGetAllUsersByCourse(String courseId, Pageable pageable) {
        return "/users?courseId=" + courseId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=id".replaceAll(":", ",");
    }
}
