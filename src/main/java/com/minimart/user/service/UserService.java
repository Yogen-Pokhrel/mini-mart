package com.minimart.user.service;

import com.minimart.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> findAll(Pageable pageable);
    User findById(int id);
    User findByEmail(String email);
    void save(User user);
    void update(int userId, User user);
    void delete(int id);
}
