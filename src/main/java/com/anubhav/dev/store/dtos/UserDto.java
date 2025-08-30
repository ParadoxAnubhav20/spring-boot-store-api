package com.anubhav.dev.store.dtos;

import com.fasterxml.jackson.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    @JsonIgnore
    private String password;
}
