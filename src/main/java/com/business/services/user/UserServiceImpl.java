package com.business.services.user;

import com.business.api.dto.user.UserCreationDto;
import com.business.api.dto.user.UserDto;
import com.business.api.services.UserService;
import com.business.converters.UserConverter;
import com.business.model.User;
import com.dal.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserConverter converter;

    @Override
    public UserDto save(UserCreationDto dto) {
        User user = converter.convert(dto);
        user = repository.save(user);
        return converter.toDto(user);
    }
}
