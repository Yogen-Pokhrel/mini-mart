package com.minimart.user.service;

import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.User;
import com.minimart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public Page<UserDetailDto> findAll(PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<User> paginatedUser = userRepository.findAll(pageable);
        return paginatedUser.map(user -> modelMapper.map(user, UserDetailDto.class));
    }

    @Override
    public UserDetailDto findById(int id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No User Exists with provided"));
        return modelMapper.map(user, UserDetailDto.class);
    }

    @Override
    public UserDetailDto findByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if(user == null) {return null;}
        return modelMapper.map(user, UserDetailDto.class);
    }

    @Override
    public User findUserByEmail(String email){
        User user = userRepository.findUserByEmail(email).orElse(null);
        if(user == null) {return null;}
        return user;
    }

    @Override
    public UserDetailDto save(CreateUserDto createDto) {
        String hashedPassword =new BCryptPasswordEncoder().encode(createDto.getPassword());
        createDto.setPassword(hashedPassword);
        User newUser = userRepository.save(modelMapper.map(createDto, User.class));
        return modelMapper.map(newUser, UserDetailDto.class);
    }

    @Override
    public void update(int userId, User user) {
        Optional<User> userOptional = userRepository.findById(userId);
        userOptional.ifPresent(userRepository::save);
    }

    @Override
    public void delete(int id) {
        userRepository.deleteById(id);
    }
}
