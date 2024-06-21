package com.minimart.user;

import com.minimart.address.dto.request.CreateAddressDto;
import com.minimart.address.entity.Address;
import com.minimart.common.CommonService;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.order.entity.OrderLineStatus;
import com.minimart.role.RoleRepository;
import com.minimart.role.entity.Role;
import com.minimart.user.dto.RegistrationType;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.request.UpdateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.User;
import com.minimart.user.entity.UserStatus;
import com.minimart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements CommonService<CreateUserDto, UpdateUserDto, UserDetailDto, Integer> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    @SuppressWarnings("unchecked")
    public Page<UserDetailDto> findAll(PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<User> paginatedUser = userRepository.findAll(pageable);
        return paginatedUser.map(user -> modelMapper.map(user, UserDetailDto.class));
    }

    @Override
    public UserDetailDto findById(Integer id) throws Exception {
        User user = userRepository.findById(id).orElseThrow(() -> new NoResourceFoundException("No User Exists with provided ID"));
        return modelMapper.map(user, UserDetailDto.class);
    }

    public UserDetailDto findByEmail(String email) {
        User user = userRepository.findUserByEmail(email).orElse(null);
        if(user == null) {return null;}
        return modelMapper.map(user, UserDetailDto.class);
    }

    @Override
    public UserDetailDto save(CreateUserDto createDto) {
        RegistrationType registrationType = RegistrationType.valueOf(createDto.getRegistrationType().toUpperCase());

        String hashedPassword =new BCryptPasswordEncoder().encode(createDto.getPassword());
        createDto.setPassword(hashedPassword);
        User newUser = userRepository.save(modelMapper.map(createDto, User.class));

        Role role = roleRepository.findBySlug(registrationType.toString());
        if(role != null){
            newUser.addRole(role);
            userRepository.save(newUser);
        }

        return modelMapper.map(newUser, UserDetailDto.class);
    }

    @Override
    public UserDetailDto update(Integer userId, UpdateUserDto updateUserDto) throws Exception {
        Optional<User> userOptional = userRepository.findById(userId);
        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();

            if (updateUserDto.getFirstName() != null) {
                user.setFirstName(updateUserDto.getFirstName());
            }
            if (updateUserDto.getLastName() != null) {
                user.setLastName(updateUserDto.getLastName());
            }
            if (updateUserDto.getEmail() != null) {
                user.setEmail(updateUserDto.getEmail());
            }
            if (updateUserDto.getPhone() != null) {
                user.setPhone(updateUserDto.getPhone());
            }
            if (updateUserDto.getPassword() != null) {
                user.setPassword(updateUserDto.getPassword());
            }

            if(updateUserDto.getStatus() != null){
                UserStatus status = UserStatus.valueOf(updateUserDto.getStatus());
                user.setStatus(status);
            }

            if (updateUserDto.getImage() != null) {
                user.setImage(updateUserDto.getImage());
            }
            user = userRepository.save(user);
        } else {
            throw new Exception("User not found");
        }
        return modelMapper.map(user,UserDetailDto.class);
    }

    @Override
    public void delete(Integer id) throws Exception {
        findById(id);
        userRepository.deleteById(id);
    }

    /*public Page<UserDetailDto> findAllDeletedUsers(PaginationDto paginationDto) {
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getSize());
        Page<User> paginatedUser = userRepository.findAllByDeletedTrue(pageable);
        return paginatedUser.map(user -> modelMapper.map(user, UserDetailDto.class));
    }*/
}
