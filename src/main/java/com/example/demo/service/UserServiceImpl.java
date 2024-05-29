package com.example.demo.service;

import com.example.demo.dto.user.UserRegistrationRequestDto;
import com.example.demo.dto.user.UserResponseDto;
import com.example.demo.exeption.RegistrationException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.ShoppingCartRepository;
import com.example.demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private ShoppingCartRepository shoppingCartRepository;
    private ShoppingCartService shoppingCartService;
    private RoleRepository roleRepository;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.email();
        if (userRepository.existsByEmail(requestDto.email())) {
            throw new RegistrationException("User with email " + email + " already exists");
        }
        User user = userMapper.toModel(requestDto);
        String encryptedPassword = passwordEncoder.encode(requestDto.password());
        user.setPassword(encryptedPassword);
        user.setRoles(Set.of((roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new RegistrationException("Can't find role USER")))));

        shoppingCartService.createShoppingCart(user);

        return userMapper.toDto(userRepository.save(user));
    }
}
