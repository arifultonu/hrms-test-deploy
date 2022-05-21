package com.hrms.acl.auth.entity.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtRequest {

    @NotBlank(message = "username must not be blank")
    String username;

    @Size(message = "password size must be 4 to 20", min = 4,max = 20)
    @NotBlank
    String password;
}
