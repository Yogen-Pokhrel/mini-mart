package com.minimart.user;

import com.minimart.common.ApiResponse;
import com.minimart.common.exception.DuplicateResourceException;
import com.minimart.common.ResponseMeta;
import com.minimart.common.dto.PaginationDto;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public ApiResponse<List<UserDetailDto>>  findAll(PaginationDto paginationDto) {
        Page<UserDetailDto> userPaginated = userService.findAll(paginationDto);
        return ApiResponse.success(
                userPaginated.getContent(),
                "Users fetched successfully",
                new ResponseMeta(
                        userPaginated.getNumber(),
                        userPaginated.getSize(),
                        userPaginated.getTotalElements(),
                        userPaginated.getTotalPages())
        );
    }

    @PostMapping
    public ApiResponse<UserDetailDto> createUser(@Valid @RequestBody CreateUserDto createDto) throws Exception {
        UserDetailDto existingUser = userService.findByEmail(createDto.getEmail());
        if(existingUser != null) {
            throw new DuplicateResourceException("User already exists");
        }
        UserDetailDto newUser = userService.save(createDto);
        return ApiResponse.success(newUser, "User created successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteUser(@PathVariable int id) {
        userService.delete(id);
        return ApiResponse.success("Successfully Deleted the ", "true", new ResponseMeta(1,1,1,1));
    }


}
