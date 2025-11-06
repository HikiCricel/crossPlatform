package com.example.crossPlatform.mapper;

import java.util.stream.Collectors;

import com.example.crossPlatform.dto.UserDTO;
import com.example.crossPlatform.dto.UserLogged;
import com.example.crossPlatform.model.Permission;
import com.example.crossPlatform.model.User;

public class UserMapper {
    public static UserDTO userToUserDTO(User user) {
        return new UserDTO(user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getRole().getAuthority(),
                user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }

    public static UserLogged userToUserLoggedDTO(User user) {
        return new UserLogged(user.getUsername(), user.getRole().getAuthority(),
                user.getRole().getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
    }
}
