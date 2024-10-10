package com.ead.course.clients;

import com.ead.course.dtos.CourseUserDto;
import com.ead.course.dtos.ResponsePageDto;
import com.ead.course.dtos.UserDto;
import com.ead.course.services.UtilsService;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.Response;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
public class AuthUserClient {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UtilsService utilsService;

    @Value("${ead.api.url.authuser}")
    String REQUEST_URL_AUTHUSER;

    public Page<UserDto> getAllCoursesByUser(UUID courseId, Pageable pageable) {
        List<UserDto> searchResult = null;
        String url = REQUEST_URL_AUTHUSER + utilsService.createUrlGetAllUsersByCourse(courseId.toString(), pageable);

        log.debug("Request URL: {}", url);
        log.info("Request URL: {}", url);

        try {
            ParameterizedTypeReference<ResponsePageDto<UserDto>> responseType = new ParameterizedTypeReference<ResponsePageDto<UserDto>>() {};
            ResponseEntity<ResponsePageDto<UserDto>> result = restTemplate.exchange(url, HttpMethod.GET, null, responseType);
            searchResult = result.getBody().getContent();
            log.debug("Response Number of Elements: {}", searchResult.size());
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage());
            e.printStackTrace();
        }

        log.info("Ending request /courses courseId {}", courseId);
        return new PageImpl<>(searchResult);
    }

    public ResponseEntity<UserDto> getOneUserById(UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId;
        log.debug("Request URL: {}", url);

        return restTemplate.exchange(url, HttpMethod.GET, null, UserDto.class);
    }

    public void postSubscriptionUserInCourse(UUID courseId, UUID userId) {
        String url = REQUEST_URL_AUTHUSER + "/users/" + userId + "/courses/subscription";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String requestBody = String.format("{\"courseId\": \"%s\"}", courseId);
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

    }

    public void deleteCourseIntoAuthUser(UUID courseId) {
        String url = REQUEST_URL_AUTHUSER + "/users/courses/" + courseId;
        log.debug("Request URL: {}", url);

        restTemplate.exchange(url, HttpMethod.DELETE, null, String.class);
    }
}
