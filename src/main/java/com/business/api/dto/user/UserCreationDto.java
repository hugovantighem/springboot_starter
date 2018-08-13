package com.business.api.dto.user;

import com.business.api.dto.CreationDto;
import lombok.Data;

@Data
public class UserCreationDto implements CreationDto {
    private String username;

    private String password;
}
