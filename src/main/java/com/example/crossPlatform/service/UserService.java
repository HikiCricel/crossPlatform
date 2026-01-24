package com.example.crossPlatform.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    public final PasswordEncoder passwordEncoder;
    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + " not found"));
        logger.info("Successfully retrieved user with ID: {}", id);
        return user;
    }

    public List<UserDTO> getUsers() {
        List<UserDTO> users = userRepository.findAll().stream().map(UserMapper::userToUserDTO).toList();
        logger.info("Successfully retrieved {} users", users.size());
        return users;
    }

    public UserDTO getUserDTO(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + id + "not found"));
        logger.info("Successfully retrieved user with ID: {}", id);
        return UserMapper.userToUserDTO(user);
    }

    public UserDTO getUserDTO(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User with username " + username + " not found"));
        logger.info("Successfully retrieved user with Username: {}", username);
        return UserMapper.userToUserDTO(user);
    }

    public User getUser(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("User with username " + username + " not found"));
        logger.info("Successfully retrieved User entity for username: {}", username);
        return user;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User updatePassword(String username, String newPassword) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        logger.info("Successfully changed password for User with Username: {}", username);
        return user;
    }
}
