package com.ead.authuser.dtos;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
public class JwtDto {

    @NotNull
    private String token;
    private String type = "Bearer";

    public JwtDto(String token) {
        this.token = token;
    }

}
