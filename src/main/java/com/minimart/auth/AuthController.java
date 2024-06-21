package com.minimart.auth;

import com.minimart.auth.dto.UserLoginDTO;
import com.minimart.auth.dto.UserLoginResponseDTO;
import com.minimart.auth.dto.UserRegisterDTO;
import com.minimart.auth.util.JWTUtil;
import com.minimart.common.ApiResponse;
import com.minimart.common.exception.DuplicateResourceException;
import com.minimart.common.exception.NoResourceFoundException;
import com.minimart.role.entity.Role;
import com.minimart.user.dto.RegistrationType;
import com.minimart.user.dto.request.CreateUserDto;
import com.minimart.user.dto.response.SimpleUserDto;
import com.minimart.user.dto.response.UserDetailDto;
import com.minimart.user.entity.UserStatus;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController()
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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
        if(Objects.equals(userRegisterDTO.getRegistrationType(), "SELLER")){
            createUserDto.setStatus(UserStatus.PENDING);
        }else{
            createUserDto.setStatus(UserStatus.APPROVED);
        }
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

        Optional<Role> neededRoleOpt = existingUser.getRoles().stream()
                .filter(role -> role.getSlug().equals(userLoginDTO.getRegistrationType()))
                .findFirst();

        if (neededRoleOpt.isEmpty()) {
            throw new NoResourceFoundException(userLoginDTO.getRegistrationType() + " role not available for the user");
        }

        Role loggedRole = neededRoleOpt.get();

        UserLoginResponseDTO userLoginResponseDTO = modelMapper.map(existingUser, UserLoginResponseDTO.class);

        existingUser.setRoles(Collections.singletonList(loggedRole));

        Map<String, Object> responseData = new HashMap<>();

        responseData.put("access_token", "Bearer " + this.jwtUtil.generateToken(existingUser));
        responseData.put("refresh_token", this.jwtUtil.generateRefreshToken(existingUser.getEmail()));

        responseData.put("userData", userLoginResponseDTO);

        return ApiResponse.success(
                responseData,
                "User logged in successfully"
        );
    }

    @GetMapping("/me")
    public ApiResponse<UserDetailDto> getCurrentUser(@AuthenticationPrincipal AuthDetails authDetails) throws Exception {
        UserDetailDto userData = authService.getLoggedInUserData(authDetails.getId());
        return ApiResponse.success(userData, "User data fetched successfully");
    }
}

