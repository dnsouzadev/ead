package com.ead.authuser.clients;

import com.ead.authuser.dtos.CourseDto;
import com.ead.authuser.dtos.ResponsePageDto;
import com.ead.authuser.services.UtilsService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class CourseClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_COURSE;

//    @Retry(name = "retryInstance", fallbackMethod = "retryFallback")
    @CircuitBreaker(name = "circuitbreakerInstance", fallbackMethod = "circuitBreakerFallback")
    public Page<CourseDto> getAllCoursesByUser(UUID userId, Pageable pageable, String token) {
        String url = REQUEST_URL_COURSE + utilsService.createUrlGetAllCoursesByUserId(userId.toString(), pageable);
        log.info("Request URL: {}", url);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        log.debug("UserId: {}", userId);
        log.debug("Pageable: {}", pageable);
        log.debug("Token: {}", token);

        ParameterizedTypeReference<ResponsePageDto<CourseDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<CourseDto>>() {};
        ResponseEntity<ResponsePageDto<CourseDto>> result = restTemplate.exchange(url, HttpMethod.GET, entity, responseType);

        ResponsePageDto<CourseDto> responseBody = result.getBody();
        if (responseBody == null || responseBody.getContent() == null) {
            log.warn("Response body is null or has no content for userId {}", userId);
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<CourseDto> searchResult = responseBody.getContent();
        log.debug("Response Number of Elements: {}", searchResult.size());
        log.info("Ending request /courses userId {}", userId);

        return new PageImpl<>(searchResult, pageable, responseBody.getTotalElements());
    }


    public Page<CourseDto> circuitBreakerFallback(UUID userId, Pageable pageable, String token, Throwable throwable) {
        log.error("Inside circuit breaker fallback: {}", throwable.toString());
        List<CourseDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult, pageable, 0);
    }


    public Page<CourseDto> retryFallback(UUID userId, Pageable pageable, String token, Throwable throwable) {
        log.error("Inside retry fallback: {}", throwable.toString());
        List<CourseDto> searchResult = new ArrayList<>();
        return new PageImpl<>(searchResult, pageable, 0);
    }

}
