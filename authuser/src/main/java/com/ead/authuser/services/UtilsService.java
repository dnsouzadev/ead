package com.ead.authuser.services;

import org.springframework.data.domain.Pageable;

public interface UtilsService {

    String createUrl(String userId, Pageable pageable);
}
