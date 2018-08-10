package com.controller;

import com.authentication.jwt.JwtTokenProvider;
import com.helper.SecurityHelper;
import com.helper.UserHelper;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;

import java.net.URL;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SecurityIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }

    @Test
    public void givenWrongAuthorizationHeader_whenRequest_thenRedirectedToLogin() throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(JwtTokenProvider.AUTHORIZATION, MediaType.APPLICATION_JSON_VALUE);

        ResponseEntity<String> response = template
                .exchange(base.toString() + "/api/ping",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), CoreMatchers.containsString("/login"));
    }

    @Test
    public void givenWrongPassword_whenAuthenticate_thenReturnError() throws Exception {
        ResponseEntity<String> response = processLogin(UserHelper.EXISTING_USERNAME, UserHelper.WRONG_PASSWORD);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void givenWrongUser_whenAuthenticate_thenReturnError() throws Exception {
        ResponseEntity<String> response = processLogin(UserHelper.WRONG_USERNAME, UserHelper.EXISTING_PASSWORD);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.FORBIDDEN));
    }

    @Test
    public void givenCredential_whenAuthenticate_thenReturnJwt() throws Exception {
        ResponseEntity<String> response = successfulLogin();
        String authorization = extractAuthorization(response);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(authorization, CoreMatchers.startsWith(JwtTokenProvider.TOKEN_TYPE));
    }

    @Test
    public void givenValidJwt_whenRequest_thenReturnOk() throws Exception {
        ResponseEntity<String> response = successfulLogin();
        String jwt = extractJwt(response);

        response = processPing(jwt);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.OK));
        assertThat(response.getBody(), equalTo("pong"));
    }

    private ResponseEntity<String> successfulLogin(){
        return processLogin(UserHelper.EXISTING_USERNAME, UserHelper.EXISTING_PASSWORD);

    }

    private String extractAuthorization(ResponseEntity<String> response){
        return response.getHeaders().get(JwtTokenProvider.AUTHORIZATION).get(0);
    }

    private String extractJwt(ResponseEntity<String> response){
        return JwtTokenProvider.extractJwt(response.getHeaders().get(JwtTokenProvider.AUTHORIZATION).get(0));
    }

    private ResponseEntity<String> processPing(String token){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(JwtTokenProvider.AUTHORIZATION, JwtTokenProvider.authorizationToken(token));

        return template
                .exchange(base.toString() + "/api/ping",
                        HttpMethod.GET,
                        new HttpEntity<>(headers),
                        String.class);
    }

    private ResponseEntity<String> processLogin(String username, String password){
        MultiValueMap<String, String> data = SecurityHelper.loginForm(
                username,
                password);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(data, headers);

        return template.postForEntity( base.toString() + "/login", request , String.class );
    }
}
