package com.examme.examme.service;

import com.examme.examme.dto.LoginRequestDto;
import com.examme.examme.dto.UserDto;
import com.examme.examme.dto.UserUpdateDto;
import com.examme.examme.entity.User;
import com.examme.examme.exception.BadRequestException;
import com.examme.examme.exception.ConflictException;
import com.examme.examme.exception.NotFoundException;
import com.examme.examme.exception.UnauthorizedException;
import com.examme.examme.enums.UserRole;
import com.examme.examme.repository.UserRepository;
import com.examme.examme.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public User registerUser(String email, String password, String fullName, UserRole role) {
        if (role != UserRole.STUDENT && role != UserRole.TEACHER) {
            throw new BadRequestException("Yalnız STUDENT və ya TEACHER qeydiyyatı mümkündür");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ConflictException("User with this email already exists");
        }
        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .fullName(fullName)
                .role(role)
                .build();
        return userRepository.save(user);
    }

    public String login(LoginRequestDto loginRequest) {
        User user = userRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
        return jwtTokenProvider.generateToken(user.getEmail(), user.getRole().name());
    }

    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return toDto(user);
    }

    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User requireUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    public User requireUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Transactional(readOnly = true)
    public List<UserDto> listAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional
    public UserDto updateUser(Long id, UserUpdateDto dto) {
        User user = requireUserById(id);
        if (dto.getFullName() != null && !dto.getFullName().isBlank()) {
            user.setFullName(dto.getFullName());
        }
        if (dto.getEmail() != null && !dto.getEmail().isBlank()) {
            userRepository.findByEmail(dto.getEmail()).ifPresent(other -> {
                if (!other.getId().equals(id)) {
                    throw new ConflictException("Email already in use");
                }
            });
            user.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return toDto(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }

    @Transactional
    public UserDto updateUserRole(Long id, UserRole newRole) {
        User user = requireUserById(id);
        user.setRole(newRole);
        return toDto(userRepository.save(user));
    }

    private UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
