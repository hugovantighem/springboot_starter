package com.business.api.dto.user;

import com.business.api.dto.Dto;
import lombok.Data;

@Data
public class UserDto implements Dto {
    private Long id;
    private String username;
    private String password;
}
