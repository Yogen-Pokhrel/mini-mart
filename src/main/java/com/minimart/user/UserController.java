package com.minimart.user;

import com.minimart.common.ApiResponse;
import com.minimart.common.exception.DuplicateResourceException;
import com.minimart.common.ResponseMeta;
import com.minimart.common.dto.PaginationDto;
import com.minimart.common.exception.DuplicateResourceException;
import com.minimart.helpers.FileUploaderService;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.request.UpdateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {
    private final UserService userService;
    private final FileUploaderService fileUploaderService;
    private final String uploadPath = "public/minimart/users/";

    @PreAuthorize("hasAnyAuthority('CUSTOMER')")
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

    @PostMapping(consumes = "multipart/form-data", produces = {"application/json"})
    public ApiResponse<UserDetailDto> createUser(@Valid @ModelAttribute CreateUserDto createUserDto) throws Exception {
        if(createUserDto.getUserImage() != null && !createUserDto.getUserImage().isEmpty()){
            File uploadedFile = fileUploaderService.upload(createUserDto.getUserImage(), uploadPath);
            createUserDto.setImage(uploadedFile.getPath());
        }
            UserDetailDto existingUser = userService.findByEmail(createUserDto.getEmail());
            if(existingUser != null) {
                throw new DuplicateResourceException("User already exists");
            }
            UserDetailDto newUser = userService.save(createUserDto);
            return ApiResponse.success(newUser, "User created successfully");
    }

    @GetMapping("/{id}")
    public ApiResponse<UserDetailDto> findById(@PathVariable int id) throws Exception {
        return ApiResponse.success(userService.findById(id), "User fetched successfully");
    }

    @PutMapping(path = "/{id}", consumes = "multipart/form-data", produces = {"application/json"})
    public ApiResponse<UserDetailDto> update(@PathVariable int id, @ModelAttribute UpdateUserDto updateUserDto) throws Exception {
        if(updateUserDto.getUserImage() != null && !updateUserDto.getUserImage().isEmpty()){
            File uploadedFile = fileUploaderService.upload(updateUserDto.getUserImage(), uploadPath);
            updateUserDto.setImage(uploadedFile.getPath());
        }
        UserDetailDto userDetailDto = userService.update(id, updateUserDto);
        return ApiResponse.success(userDetailDto, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteUser(@PathVariable int id) throws Exception {
        userService.delete(id);
        return ApiResponse.success("Successfully Deleted the ", "true");
    }


}
