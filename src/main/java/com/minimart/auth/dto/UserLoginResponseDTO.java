package com.minimart.auth.dto;

import com.minimart.role.dto.RoleResponseDto;
import com.minimart.role.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLoginResponseDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String image;
    private List<RoleResponseDto> roles;
}
