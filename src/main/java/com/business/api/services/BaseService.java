package com.business.api.services;

import com.business.api.dto.CreationDto;
import com.business.api.dto.Dto;

public interface BaseService<E extends Dto, C extends CreationDto> {

    E save(C dto);
}
