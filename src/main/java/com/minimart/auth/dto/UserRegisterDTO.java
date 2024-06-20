package com.minimart.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.minimart.common.validation.ValidPassword;
import com.minimart.helpers.validators.ValidEnum;
import com.minimart.user.dto.RegistrationType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRegisterDTO {
    @NotBlank(message = "First name is mandatory")
    @Size(min = 1, max = 50, message = "First name must be between 1 and 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must be less than or equal to 50 characters")
    private String lastName;

    @NotBlank(message = "email is mandatory")
    @Email(message = "email should be valid")
    private String email;

    @NotBlank(message = "password is mandatory")
    @ValidPassword
    private String password;

    @NotBlank(message = "Role type is mandatory")
    @ValidEnum(enumClass = RegistrationType.class, message = "Invalid registration type")
    private String registrationType;
}
