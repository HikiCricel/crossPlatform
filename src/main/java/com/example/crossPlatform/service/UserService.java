package com.example.crossPlatform.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.crossPlatform.dto.UserDTO;
import com.example.crossPlatform.exeptions.ResourceNotFoundException;
import com.example.crossPlatform.mapper.UserMapper;
import com.example.crossPlatform.model.User;
import com.example.crossPlatform.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
        return user;
    }

    public List<UserDTO> getUsers() {
        return userRepository.findAll().stream().map(UserMapper::userToUserDTO).toList();
    }

    public UserDTO getUserDto(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        return UserMapper.userToUserDTO(user);
    }

    public UserDTO getUserDto(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User with username " + username + "not found"));
        return UserMapper.userToUserDTO(user);
    }
}
