package com.controller;

import com.business.api.dto.user.UserCreationDto;
import com.dal.persistence.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class UserControllerIT extends ControllerIT{

    private String auth = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiaXNzIjoiYXV0aC1zZXJ2aWNlLWp3dCIsImlhdCI6MTUzNDE1MjI4MywiZXhwIjoxNTM0NzU3MDgzLCJyb2xlcyI6IlJPTEVfVVNFUiJ9.knw1OaCrcJvLvVaxEPyS_e8iiJJ1MHcfRHOJW26Iai8";

    @Autowired
    private UserRepository userRepository;

    @Test
    public void givenUserInfos_whenPost_thenReturnOk() throws Exception {
        String username = "Foo";
        assertNull(userRepository.findByUsername(username));
        ResponseEntity<String> response = createUser(username, "Bar");
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertNotNull(userRepository.findByUsername(username));
    }


    private ResponseEntity<String> createUser(String username, String password) throws JsonProcessingException {
        UserCreationDto dto = new UserCreationDto();
        dto.setUsername(username);
        dto.setPassword(password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(jwtTokenProvider.headerKey(), auth);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(dto), headers);

        return template.postForEntity( base.toString() + UserController.ENDPOINT, request , String.class );
    }
}
