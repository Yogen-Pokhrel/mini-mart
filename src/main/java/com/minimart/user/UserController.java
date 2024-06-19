package com.minimart.user;

import com.minimart.common.ApiResponse;
import com.minimart.common.ResponseMeta;
import com.minimart.user.dto.CreateUserDto;
import com.minimart.user.entity.User;
import com.minimart.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<User>>  findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<User> usersPage = userService.findAll(pageable);
        return ApiResponse.success(
                usersPage.getContent(),
                "true",
                new ResponseMeta(
                        usersPage.getNumber(),
                        usersPage.getSize(),
                        usersPage.getTotalElements(),
                        usersPage.getTotalPages())
        );
    }

    @PostMapping
    public ApiResponse<String> createUser(@Valid @RequestBody CreateUserDto createDto) {
        System.out.println(createDto);
        return ApiResponse.success("Hello World!", "true", new ResponseMeta(10,10,10,10));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return ApiResponse.success("Successfully Deleted the ", "true", new ResponseMeta(1,1,1,1));
    }


}
