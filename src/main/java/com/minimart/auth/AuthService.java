package com.minimart.auth;

import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.entity.User;
import com.minimart.user.service.UserServiceImpl;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    private final UserServiceImpl userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public AuthService(UserServiceImpl userService) {
        this.userService = userService;
    }

    @Override
    public AuthDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userService.findUserByEmail(username);
        if (user != null) {
            return new AuthDetails(user);
        }
        return null;
    }

    public User register(User user) {
        CreateUserDto createUserDto = this.modelMapper.map(user, CreateUserDto.class);
        return this.modelMapper.map(this.userService.save(createUserDto), User.class);
    }
}
