package com.minimart.auth;

import com.minimart.user.UserService;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.User;
import com.minimart.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public AuthService(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public AuthDetails loadUserByUsername(String username) {
        Optional<User> user = this.userRepository.findUserByEmail(username);
        return user.map(AuthDetails::new).orElse(null);
    }

    public UserDetailDto register(CreateUserDto user) {
        return userService.save(user);
    }
}
