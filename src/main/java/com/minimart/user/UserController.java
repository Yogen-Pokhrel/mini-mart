package com.minimart.user;

import com.minimart.common.ApiResponse;
import com.minimart.common.ResponseMeta;
import com.minimart.user.dto.CreateUserDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @GetMapping
    public ApiResponse<String>  findAll() {
        return ApiResponse.success("Hello World!", "true",  new ResponseMeta(10,10,10,10));
    }

    @PostMapping
    public ApiResponse<String> createUser(@Valid @RequestBody CreateUserDto createDto) {
        System.out.println(createDto);
        return ApiResponse.success("Hello World!", "true", new ResponseMeta(10,10,10,10));
    }
}
