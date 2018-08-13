package com.controller;

import com.authentication.jwt.JwtTokenProvider;
import com.config.settings.JwtSettings;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ControllerIT {

    @LocalServerPort
    protected int port;

    protected URL base;

    @Autowired
    protected TestRestTemplate template;

    @Autowired
    protected JwtSettings settings;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @Autowired
    protected ObjectMapper objectMapper;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/");
    }
}
