package com.ead.authuser.services.impl;

import com.ead.authuser.services.UtilsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class UtilsServiceImpl implements UtilsService {

    public String createUrlGetAllCoursesByUserId(String userId, Pageable pageable) {
        return "/courses?userId=" + userId + "&page=" + pageable.getPageNumber() + "&size=" + pageable.getPageSize() + "&sort=id".replaceAll(":", ",");
    }
}
