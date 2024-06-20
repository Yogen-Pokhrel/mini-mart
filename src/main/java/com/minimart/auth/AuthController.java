package com.minimart.auth;

import com.minimart.auth.dto.UserLoginDTO;
import com.minimart.auth.dto.UserRegisterDTO;
import com.minimart.auth.util.JWTUtil;
import com.minimart.common.ApiResponse;
import com.minimart.common.exception.DuplicateResourceException;
import com.minimart.role.entity.Role;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController()
@RequestMapping("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final BCryptPasswordEncoder passwordEncoder;

    private JWTUtil jwtUtil;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public AuthController(AuthService authService, BCryptPasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ApiResponse<UserDetailDto> register(@Valid @RequestBody UserRegisterDTO userRegisterDTO) throws Exception {

        if (this.authService.loadUserByUsername(userRegisterDTO.getEmail()) != null) {
            throw new DuplicateResourceException("Email already exists");
        }
        userRegisterDTO.setPassword(this.passwordEncoder.encode(userRegisterDTO.getPassword()));
        CreateUserDto createUserDto = this.modelMapper.map(userRegisterDTO, CreateUserDto.class);
        return ApiResponse.success(
                this.authService.register(createUserDto),
                "User registered successfully"
        );
    }

    @PostMapping("/login")
    public ApiResponse<? > login(@Valid @RequestBody UserLoginDTO userLoginDTO) throws Exception {
        AuthDetails existingUser = this.authService.loadUserByUsername(userLoginDTO.getEmail());
        if (existingUser == null) {
            throw new DuplicateResourceException("User not registered.");
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("access_token", "Bearer " + this.jwtUtil.generateToken(existingUser));
        responseData.put("refresh_token", this.jwtUtil.generateRefreshToken(existingUser.getEmail()));
        responseData.put("role", existingUser.getRole().stream().map(Role::getSlug).toList());

        return ApiResponse.success(
                responseData,
                "User logged in successfully"
        );
    }
}

