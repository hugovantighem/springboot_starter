package com.business.converters;

import com.business.model.User;
import com.business.api.dto.user.UserCreationDto;
import com.business.api.dto.user.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    protected ModelMapper mapper = new ModelMapper();

    public User convert(UserCreationDto dto){
        return mapper.map(dto, User.class);
    }

    public UserDto toDto(User user) {
        return mapper.map(user, UserDto.class);
    }
}
