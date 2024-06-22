package com.minimart.user.dto.request;

import com.minimart.helpers.validators.ValidEnum;
import com.minimart.user.entity.UserStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private MultipartFile userImage;
    private String image;

    @ValidEnum(enumClass = UserStatus.class, message = "Please provide a valid status")
    private String status;

}
