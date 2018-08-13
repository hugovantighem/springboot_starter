package com.config.settings;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Validated
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = JwtSettings.PREFIX)
public class JwtSettings {
    public static final String PREFIX = "authentication.jwt";

    @Valid
    @NotEmpty
    private String alg;
    @Valid
    @NotEmpty
    private String secret;
    @Valid
    @NotNull
    private Integer jwtExpirationInMinutes; // 7 days
    @Valid
    @NotEmpty
    private String authorization;
    @Valid
    @NotEmpty
    private String tokenType;
    @Valid
    @NotEmpty
    private String authServiceJwt;
}
