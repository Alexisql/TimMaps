package com.alexis.timmaps.data.datasource.mapper;

import com.alexis.timmaps.data.datasource.model.UserDto;
import com.alexis.timmaps.domain.model.User;

public class UserMapper {
    public static User toDomain(UserDto dto) {
        return new User(dto.getUsername());
    }
}
