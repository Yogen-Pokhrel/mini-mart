package com.minimart.auth.dto;

import com.minimart.common.validation.ValidPassword;
import com.minimart.helpers.validators.ValidEnum;
import com.minimart.user.dto.RegistrationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserLoginDTO {
    @NotBlank(message = "email is required")
    @Email
    private String email;
    @NotBlank(message = "password is required")
//    @ValidPassword
    private String password;

    @NotBlank(message = "Role type is mandatory")
    @ValidEnum(enumClass = RegistrationType.class, message = "Invalid registration type")
    private String registrationType;

    public UserLoginDTO() {
    }

    public UserLoginDTO(String email, String password, String registrationType) {
        this.email = email;
        this.password = password;
        this.registrationType = registrationType;
    }
}