package com.minimart.user.service;

import com.minimart.user.entity.User;
import com.minimart.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public Page<User> findAll(Pageable pageable) {
        long totalUsers = userRepository.count();

        return userRepository.findAll(pageable);
    }

    @Override
    public User findById(int id) {
        Optional<User> user = userRepository.findById(id);
        return user.orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        Optional<User> user = userRepository.findUserByEmail(email);
        return user.orElse(null);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
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
