package com.minimart.user.service;

import com.minimart.common.dto.PaginationDto;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.dto.response.UserListDto;
import com.minimart.user.entity.User;
import org.springframework.data.domain.Page;

public interface UserService {
    Page<UserDetailDto> findAll(PaginationDto paginationDto);
    UserDetailDto findById(int id) throws Exception;
    UserDetailDto findByEmail(String email);
    UserDetailDto save(CreateUserDto createDto);
    void update(int userId, User user);
    void delete(int id);
}
