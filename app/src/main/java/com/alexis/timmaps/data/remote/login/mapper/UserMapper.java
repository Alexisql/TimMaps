package com.alexis.timmaps.data.remote.login.mapper;

import com.alexis.timmaps.data.remote.login.model.UserDto;
import com.alexis.timmaps.domain.login.model.User;

public class UserMapper {
    public static User toDomain(UserDto dto) {
        return new User(dto.getUsername());
    }
}
